package com.gomo.eventconsumer.consumer;

import static com.gomo.eventconsumer.point.domain.model.SourceType.*;
import static com.gomo.eventconsumer.point.domain.model.TransactionType.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomo.eventconsumer.common.util.JsonParser;
import com.gomo.eventconsumer.event.EventEntry;
import com.gomo.eventconsumer.event.EventStatus;
import com.gomo.eventconsumer.event.repository.EventEntryRepository;
import com.gomo.eventconsumer.event.repository.PointQuestCompletedSuccessEventRepository;
import com.gomo.eventconsumer.point.domain.model.TransactorId;
import com.gomo.eventconsumer.point.domain.service.PointService;
import com.gomo.eventconsumer.quest.event.PointQuestCompletedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointQuestCompletedEventConsumer {

	private final EventEntryRepository eventEntryRepository;
	private final PointQuestCompletedSuccessEventRepository successEventRepository;
	private final PointService pointService;

	@RabbitListener(queues = "quest-completed-point-queue")
	@Transactional(rollbackFor = Exception.class)
	public void handleEvent(EventEntry eventEntry) {
		try {
			if(successEventRepository.existsByEventEntryId(eventEntry.getId())) {
				return;
			}
			successEventRepository.saveProcessedEventEntry(eventEntry.getId());

			PointQuestCompletedEvent event = JsonParser.fromJson(eventEntry.getPayload(), PointQuestCompletedEvent.class);
			pointService.create(TransactorId.of(event.getParticipantId().getId()), QUEST, GAIN, event.getPointReward().getAmount());

			eventEntry.update(EventStatus.COMPLETED);
			eventEntryRepository.save(eventEntry);
		} catch (Exception e) {
			// TODO <jhl221123>: 비즈니스 예외, 동시성 예외 등 세부적으로 처리하고, 실패 전략을 구상해야 합니다.
			log.error("[PointQuestCompletedEventConsumer] Failed to process event id={}, error={}", eventEntry.getId(), e.getMessage());
			throw e;
		}
	}
}
