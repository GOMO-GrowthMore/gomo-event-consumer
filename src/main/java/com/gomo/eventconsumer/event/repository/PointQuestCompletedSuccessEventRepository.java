package com.gomo.eventconsumer.event.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PointQuestCompletedSuccessEventRepository extends SuccessEventRepository {

	public PointQuestCompletedSuccessEventRepository(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate, "point_quest_completed_success_event");
	}
}
