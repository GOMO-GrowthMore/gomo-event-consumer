package com.gomo.eventconsumer.consumer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import com.gomo.eventconsumer.common.util.JsonParser;
import com.gomo.eventconsumer.event.EventEntry;
import com.gomo.eventconsumer.event.EventStatus;
import com.gomo.eventconsumer.event.repository.EventEntryRepository;
import com.gomo.eventconsumer.event.repository.PointQuestCompletedSuccessEventRepository;
import com.gomo.eventconsumer.point.domain.service.PointService;
import com.gomo.eventconsumer.quest.ParticipantId;
import com.gomo.eventconsumer.quest.PointReward;
import com.gomo.eventconsumer.quest.event.PointQuestCompletedEvent;

@DisplayName("[unit]: 퀘스트 완료(포인트) 이벤트 처리 테스트")
@ExtendWith(MockitoExtension.class)
public class PointQuestCompletedEventConsumerTest {

	@InjectMocks
	private PointQuestCompletedEventConsumer sut;

	@Mock
	private EventEntryRepository eventEntryRepository;

	@Mock
	private PointQuestCompletedSuccessEventRepository successEventRepository;

	@Mock
	private PointService pointService;

	@DisplayName("포인트 생성 이벤트를 처리한다.")
	@Test
	void event_process() {
		EventEntry eventEntry = EventEntry.of("PointQuestCompletedEvent", "payload", 1L);
		PointQuestCompletedEvent event = PointQuestCompletedEvent.of(ParticipantId.of(UUID.randomUUID()), PointReward.of(2), 1L);
		doReturn(false).when(successEventRepository).existsByEventEntryId(any());

		try (MockedStatic<JsonParser> mockedStatic = mockStatic(JsonParser.class)) {
			mockedStatic.when(() -> JsonParser.fromJson("payload", PointQuestCompletedEvent.class)).thenReturn(event);

			sut.handleEvent(eventEntry);

			verify(pointService, times(1)).create(any(), any(), any(), anyInt());
			assertThat(eventEntry.getEventStatus()).isEqualTo(EventStatus.COMPLETED);
			verify(eventEntryRepository, times(1)).save(any());
		}
	}

	@DisplayName("이미 처리된 이벤트는 처리되지 않는다.")
	@Test
	void do_not_process_duplicated_event() {
		EventEntry eventEntry = EventEntry.of("PointQuestCompletedEvent", "payload", 1L);
		doReturn(true).when(successEventRepository).existsByEventEntryId(any());

		sut.handleEvent(eventEntry);

		verifyNoInteractions(pointService);
		verifyNoInteractions(eventEntryRepository);
	}

	@DisplayName("포인트 생성 작업에 실패한다.")
	@Test
	void cannot_process_event_by_point() {
		EventEntry eventEntry = EventEntry.of("PointQuestCompletedEvent", "payload", 1L);
		PointQuestCompletedEvent event = PointQuestCompletedEvent.of(ParticipantId.of(UUID.randomUUID()), PointReward.of(2), 1L);
		doThrow(new IllegalStateException("Point service failure")).when(pointService).create(any(), any(), any(), anyInt());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(PointQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(IllegalStateException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verify(pointService, times(1)).create(any(), any(), any(), anyInt());
			verifyNoInteractions(eventEntryRepository);
		}
	}

	@DisplayName("성공 이벤트 저장 도중 데이터 베이스 작업이 실패한다.")
	@Test
	void cannot_process_event_by_success_event() {
		EventEntry eventEntry = EventEntry.of("PointQuestCompletedEvent", "payload", 1L);
		PointQuestCompletedEvent event = PointQuestCompletedEvent.of(ParticipantId.of(UUID.randomUUID()), PointReward.of(2), 1L);
		doThrow(new DataAccessException("Success event save error") {}).when(successEventRepository).saveProcessedEventEntry(any());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(PointQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(DataAccessException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verifyNoInteractions(pointService);
			verifyNoInteractions(eventEntryRepository);
		}
	}

	@DisplayName("이벤트 상태 변경 도중 데이터 베이스 작업이 실패한다.")
	@Test
	void cannot_process_event_by_event_entry() {
		EventEntry eventEntry = EventEntry.of("PointQuestCompletedEvent", "payload", 1L);
		PointQuestCompletedEvent event = PointQuestCompletedEvent.of(ParticipantId.of(UUID.randomUUID()), PointReward.of(2), 1L);
		doThrow(new DataAccessException("Event entry save error") {}).when(eventEntryRepository).save(any());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(PointQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(DataAccessException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verify(pointService, times(1)).create(any(), any(), any(), anyInt());
			verify(eventEntryRepository, times(1)).save(any());
		}
	}
}
