package com.gomo.eventconsumer.common.event;

public abstract class Event {

	private long timestamp;

	protected Event() {}

	protected Event(long timestamp) {
		this.timestamp = timestamp;
	}
}
