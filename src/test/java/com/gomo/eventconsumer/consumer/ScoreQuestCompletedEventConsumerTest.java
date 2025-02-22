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
import com.gomo.eventconsumer.event.repository.ScoreQuestCompletedSuccessEventRepository;
import com.gomo.eventconsumer.interest.domain.service.ProficiencyService;
import com.gomo.eventconsumer.point.domain.service.PointService;
import com.gomo.eventconsumer.quest.ParticipantId;
import com.gomo.eventconsumer.quest.PointReward;
import com.gomo.eventconsumer.quest.ScoreReward;
import com.gomo.eventconsumer.quest.SubjectId;
import com.gomo.eventconsumer.quest.event.PointQuestCompletedEvent;
import com.gomo.eventconsumer.quest.event.ScoreQuestCompletedEvent;

@DisplayName("[unit]: 퀘스트 완료(점수) 이벤트 처리 테스트")
@ExtendWith(MockitoExtension.class)
public class ScoreQuestCompletedEventConsumerTest {

	@InjectMocks
	private ScoreQuestCompletedEventConsumer sut;

	@Mock
	private EventEntryRepository eventEntryRepository;

	@Mock
	private ScoreQuestCompletedSuccessEventRepository successEventRepository;

	@Mock
	private ProficiencyService proficiencyService;

	@DisplayName("숙련도 향상 이벤트를 처리한다.")
	@Test
	void event_process() {
		EventEntry eventEntry = EventEntry.of("ScoreQuestCompletedEvent", "payload", 1L);
		ScoreQuestCompletedEvent event = ScoreQuestCompletedEvent.of(UUID.randomUUID(), SubjectId.of(UUID.randomUUID()), ScoreReward.of(2), 1L);
		doReturn(false).when(successEventRepository).existsByEventEntryId(any());

		try (MockedStatic<JsonParser> mockedStatic = mockStatic(JsonParser.class)) {
			mockedStatic.when(() -> JsonParser.fromJson("payload", ScoreQuestCompletedEvent.class)).thenReturn(event);

			sut.handleEvent(eventEntry);

			verify(proficiencyService, times(1)).adjust(any(), anyInt());
			assertThat(eventEntry.getEventStatus()).isEqualTo(EventStatus.COMPLETED);
			verify(eventEntryRepository, times(1)).save(any());
		}
	}

	@DisplayName("이미 처리된 이벤트는 처리되지 않는다.")
	@Test
	void do_not_process_duplicated_event() {
		EventEntry eventEntry = EventEntry.of("ScoreQuestCompletedEvent", "payload", 1L);
		doReturn(true).when(successEventRepository).existsByEventEntryId(any());

		sut.handleEvent(eventEntry);

		verifyNoInteractions(proficiencyService);
		verifyNoInteractions(eventEntryRepository);
	}

	@DisplayName("숙련도 향상 작업에 실패한다.")
	@Test
	void cannot_process_event_by_score() {
		EventEntry eventEntry = EventEntry.of("ScoreQuestCompletedEvent", "payload", 1L);
		ScoreQuestCompletedEvent event = ScoreQuestCompletedEvent.of(UUID.randomUUID(), SubjectId.of(UUID.randomUUID()), ScoreReward.of(2), 1L);
		doThrow(new IllegalStateException("Proficiency service failure")).when(proficiencyService).adjust(any(), anyInt());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(ScoreQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(IllegalStateException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verify(proficiencyService, times(1)).adjust(any(), anyInt());
			verifyNoInteractions(eventEntryRepository);
		}
	}

	@DisplayName("성공 이벤트 저장 도중 데이터 베이스 작업이 실패한다.")
	@Test
	void cannot_process_event_by_success_event() {
		EventEntry eventEntry = EventEntry.of("ScoreQuestCompletedEvent", "payload", 1L);
		ScoreQuestCompletedEvent event = ScoreQuestCompletedEvent.of(UUID.randomUUID(), SubjectId.of(UUID.randomUUID()), ScoreReward.of(2), 1L);
		doThrow(new DataAccessException("Success event save error") {}).when(successEventRepository).saveProcessedEventEntry(any());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(ScoreQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(DataAccessException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verifyNoInteractions(proficiencyService);
			verifyNoInteractions(eventEntryRepository);
		}
	}

	@DisplayName("이벤트 상태 변경 도중 데이터 베이스 작업이 실패한다.")
	@Test
	void cannot_process_event_by_event_entry() {
		EventEntry eventEntry = EventEntry.of("ScoreQuestCompletedEvent", "payload", 1L);
		ScoreQuestCompletedEvent event = ScoreQuestCompletedEvent.of(UUID.randomUUID(), SubjectId.of(UUID.randomUUID()), ScoreReward.of(2), 1L);
		doThrow(new DataAccessException("Event entry save error") {}).when(eventEntryRepository).save(any());

		try (MockedStatic<JsonParser> mockedJsonParser = mockStatic(JsonParser.class)) {
			mockedJsonParser.when(() -> JsonParser.fromJson(any(), eq(ScoreQuestCompletedEvent.class))).thenReturn(event);

			assertThrows(DataAccessException.class, () -> sut.handleEvent(eventEntry));

			verify(successEventRepository, times(1)).saveProcessedEventEntry(any());
			verify(proficiencyService, times(1)).adjust(any(), anyInt());
			verify(eventEntryRepository, times(1)).save(any());
		}
	}
}
