/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.*;
import com.michalkolos.bicyclecycles.persistence.repository.*;
import com.michalkolos.bicyclecycles.utils.NextbikeIntegrityChecks;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
public class SampleDao {

	private final SampleRepository sampleRepository;

	@Autowired
	public SampleDao(SampleRepository sampleRepository) {
		this.sampleRepository = sampleRepository;
	}

	public Sample create() {
		return new Sample();
	}

	public Optional<Sample> getPrevious() {
		return sampleRepository.findFirstByOrderByTimestampDesc();
	}


	public void persistWeather(Sample sample) {
		// TODO: Implement persisting weather data.
	}

	public Sample save(Sample sample) {
		// TODO: Implement saving of samples to database.
		return sample;
	}
}
