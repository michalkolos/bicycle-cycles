/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.utils;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Log4j2
public class NextbikeIntegrityChecks {

	// TODO: Stop using exception for duplicates detection
	public int bikeNumbersUnique(Sample sample) {
		AtomicInteger repeatCount = new AtomicInteger(0);
		Set<Long> bikeNumbers = new HashSet<>();

		sample.getBikeStates().stream()
				.map(BikeState::getBike)
				.map(Bike::getNumber)
				.forEach(number -> {
					try {
						bikeNumbers.add(number);
					}catch (IllegalArgumentException  e) {
						repeatCount.incrementAndGet();
					}
				});

		return repeatCount.intValue();
	}
}
