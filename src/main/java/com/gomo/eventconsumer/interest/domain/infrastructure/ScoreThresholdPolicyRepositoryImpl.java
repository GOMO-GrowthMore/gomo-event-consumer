package com.gomo.eventconsumer.interest.domain.infrastructure;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.gomo.eventconsumer.interest.domain.model.ScoreThresholdPolicy;
import com.gomo.eventconsumer.interest.domain.repository.ScoreThresholdPolicyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ScoreThresholdPolicyRepositoryImpl implements ScoreThresholdPolicyRepository {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<ScoreThresholdPolicy> findAll() {
		String sql = "select * from score_threshold_policy";

		return jdbcTemplate.query(sql, (rs, rowNum) -> ScoreThresholdPolicy.of(rs.getInt("level"), rs.getInt("threshold")));
	}
}
