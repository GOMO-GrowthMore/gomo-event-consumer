package com.gomo.eventconsumer.event.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class SuccessEventRepository {

	protected final JdbcTemplate jdbcTemplate;
	private final String tableName;

	protected SuccessEventRepository(JdbcTemplate jdbcTemplate, String tableName) {
		this.jdbcTemplate = jdbcTemplate;
		this.tableName = tableName;
	}

	public void saveProcessedEventEntry(Long eventEntryId) {
		String sql = "INSERT INTO " + tableName + " (event_entry_id) VALUES (?)";
		jdbcTemplate.update(sql, eventEntryId);
	}

	public boolean existsByEventEntryId(Long eventEntryId) {
		String sql = "SELECT 1 FROM " + tableName + " WHERE event_entry_id = ? FOR UPDATE";
		List<Integer> result = jdbcTemplate.query(sql, new Object[]{eventEntryId}, (rs, rowNum) -> rs.getInt(1));
		return !result.isEmpty();
	}
}
