package com.gomo.eventconsumer.event.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StreakQuestCompletedSuccessEventRepository extends SuccessEventRepository {

	public StreakQuestCompletedSuccessEventRepository(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "streak_quest_completed_success_event");
	}
}
