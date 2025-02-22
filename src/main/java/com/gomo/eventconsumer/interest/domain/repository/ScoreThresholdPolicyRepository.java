package com.gomo.eventconsumer.interest.domain.repository;

import java.util.List;

import com.gomo.eventconsumer.interest.domain.model.ScoreThresholdPolicy;

public interface ScoreThresholdPolicyRepository {

	List<ScoreThresholdPolicy> findAll();
}
