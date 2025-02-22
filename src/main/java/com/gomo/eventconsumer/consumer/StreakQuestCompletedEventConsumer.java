package com.gomo.eventconsumer.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomo.eventconsumer.common.util.JsonParser;
import com.gomo.eventconsumer.common.util.UUIDGenerator;
import com.gomo.eventconsumer.event.EventEntry;
import com.gomo.eventconsumer.event.EventStatus;
import com.gomo.eventconsumer.event.repository.EventEntryRepository;
import com.gomo.eventconsumer.event.repository.StreakQuestCompletedSuccessEventRepository;
import com.gomo.eventconsumer.quest.event.StreakQuestCompletedEvent;
import com.gomo.eventconsumer.streak.domain.model.AchieverId;
import com.gomo.eventconsumer.streak.domain.model.Streak;
import com.gomo.eventconsumer.streak.domain.model.StreakId;
import com.gomo.eventconsumer.streak.domain.model.StreakType;
import com.gomo.eventconsumer.streak.domain.service.StreakService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StreakQuestCompletedEventConsumer {

	private final EventEntryRepository eventEntryRepository;
	private final StreakQuestCompletedSuccessEventRepository successEventRepository;
	private final StreakService streakService;

	@RabbitListener(queues = "quest-completed-streak-queue")
	@Transactional(rollbackFor = Exception.class)
	public void handleEvent(EventEntry eventEntry) {
		try {
			if(successEventRepository.existsByEventEntryId(eventEntry.getId())) {
				return;
			}
			successEventRepository.saveProcessedEventEntry(eventEntry.getId());

			StreakQuestCompletedEvent event = JsonParser.fromJson(eventEntry.getPayload(), StreakQuestCompletedEvent.class);
			streakService.fill(createStreak(event));

			eventEntry.update(EventStatus.COMPLETED);
			eventEntryRepository.save(eventEntry);
		} catch (Exception e) {
			log.error("[StreakQuestCompletedEventConsumer] Failed to process event id={}, error={}", eventEntry.getId(), e.getMessage());
			throw e;
		}
	}

	private static Streak createStreak(StreakQuestCompletedEvent event) {
		return Streak.of(
			StreakId.of(UUIDGenerator.generate()),
			AchieverId.of(event.getParticipantId().getId()),
			StreakType.valueOf(event.getQuestType().name()),
			event.getQuestCompletedDateTime().toLocalDate(),
			1
		);
	}
}
