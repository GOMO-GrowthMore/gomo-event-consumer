package com.gomo.eventconsumer.common.util;

import java.util.UUID;

import com.fasterxml.uuid.Generators;

public class UUIDGenerator {

	public static UUID generate() {
		return Generators.timeBasedEpochRandomGenerator().generate();
	}
}
