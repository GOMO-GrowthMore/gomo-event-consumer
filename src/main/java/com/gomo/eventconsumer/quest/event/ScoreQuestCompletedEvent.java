package com.gomo.eventconsumer.quest.event;

import java.util.UUID;

import com.gomo.eventconsumer.common.event.Event;
import com.gomo.eventconsumer.quest.ScoreReward;
import com.gomo.eventconsumer.quest.SubjectId;

import lombok.Getter;

@Getter
public class ScoreQuestCompletedEvent extends Event {

	private UUID memberId;
	private SubjectId subjectId;
	private ScoreReward scoreReward;

	private ScoreQuestCompletedEvent() {
		super();
	}

	private ScoreQuestCompletedEvent(
		UUID memberId,
		SubjectId subjectId,
		ScoreReward scoreReward,
		long timestamp
	) {
		super(timestamp);
		this.memberId = memberId;
		this.subjectId = subjectId;
		this.scoreReward = scoreReward;
	}

	public static ScoreQuestCompletedEvent of(
		UUID memberId,
		SubjectId subjectId,
		ScoreReward scoreReward,
		long timestamp
	) {
		return new ScoreQuestCompletedEvent(memberId, subjectId, scoreReward, timestamp);
	}
}
