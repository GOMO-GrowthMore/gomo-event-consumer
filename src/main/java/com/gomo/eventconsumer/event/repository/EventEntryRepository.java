package com.gomo.eventconsumer.event.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomo.eventconsumer.event.EventEntry;
import com.gomo.eventconsumer.event.EventStatus;

public interface EventEntryRepository extends JpaRepository<EventEntry, Long> {

	List<EventEntry> findByEventStatus(EventStatus status);
}
