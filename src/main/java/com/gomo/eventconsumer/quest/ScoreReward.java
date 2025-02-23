package com.gomo.eventconsumer.quest;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ScoreReward {

	private int score;

	protected ScoreReward() {}

	private ScoreReward(int score) {
		this.score = score;
	}

	public static ScoreReward of(int score) {
		return new ScoreReward(score);
	}
}
