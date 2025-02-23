package com.gomo.eventconsumer.interest.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomo.eventconsumer.interest.domain.model.Interest;
import com.gomo.eventconsumer.interest.domain.model.InterestId;
import com.gomo.eventconsumer.interest.domain.model.RegistrantId;

public interface InterestRepository extends JpaRepository<Interest, InterestId> {

	List<Interest> findAllByRegistrantId(RegistrantId registrantId);

	List<Interest> findAllByIdIsIn(Set<InterestId> interestIds);
}
