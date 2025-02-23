package com.gomo.eventconsumer.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomo.eventconsumer.common.util.JsonParser;
import com.gomo.eventconsumer.event.EventEntry;
import com.gomo.eventconsumer.event.EventStatus;
import com.gomo.eventconsumer.event.repository.EventEntryRepository;
import com.gomo.eventconsumer.event.repository.ScoreQuestCompletedSuccessEventRepository;
import com.gomo.eventconsumer.interest.domain.model.InterestId;
import com.gomo.eventconsumer.interest.domain.service.ProficiencyService;
import com.gomo.eventconsumer.quest.event.ScoreQuestCompletedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScoreQuestCompletedEventConsumer {

	private final EventEntryRepository eventEntryRepository;
	private final ScoreQuestCompletedSuccessEventRepository successEventRepository;
	private final ProficiencyService proficiencyService;

	@RabbitListener(queues = "quest-completed-score-queue")
	@Transactional(rollbackFor = Exception.class)
	public void handleEvent(EventEntry eventEntry) {
		try {
			if(successEventRepository.existsByEventEntryId(eventEntry.getId())) {
				return;
			}
			successEventRepository.saveProcessedEventEntry(eventEntry.getId());

			ScoreQuestCompletedEvent event = JsonParser.fromJson(eventEntry.getPayload(), ScoreQuestCompletedEvent.class);
			proficiencyService.adjust(InterestId.of(event.getSubjectId().getId()), event.getScoreReward().getScore());

			eventEntry.update(EventStatus.COMPLETED);
			eventEntryRepository.save(eventEntry);
		} catch (Exception e) {
			log.error("[ScoreQuestCompletedEventConsumer] Failed to process event id={}, error={}", eventEntry.getId(), e.getMessage());
			throw e;
		}
	}
}
