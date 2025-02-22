package com.gomo.eventconsumer.quest.event;

import com.gomo.eventconsumer.common.event.Event;
import com.gomo.eventconsumer.quest.ParticipantId;
import com.gomo.eventconsumer.quest.PointReward;

import lombok.Getter;

@Getter
public class PointQuestCompletedEvent extends Event {

	private ParticipantId participantId;
	private PointReward pointReward;

	private PointQuestCompletedEvent() {
		super();
	}

	private PointQuestCompletedEvent(
		ParticipantId participantId,
		PointReward pointReward,
		long timestamp
	) {
		super(timestamp);
		this.participantId = participantId;
		this.pointReward = pointReward;
	}

	public static PointQuestCompletedEvent of (
		ParticipantId participantId,
		PointReward pointReward,
		long timestamp
	) {
		return new PointQuestCompletedEvent(participantId, pointReward, timestamp);
	}
}
