package com.gomo.eventconsumer.event.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ScoreQuestCompletedSuccessEventRepository extends SuccessEventRepository {

	public ScoreQuestCompletedSuccessEventRepository(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "score_quest_completed_success_event");
	}
}
