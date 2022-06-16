/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.*;
import com.michalkolos.bicyclecycles.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
public class SampleDao {

	private final SampleRepository sampleRepository;
	private final BikeStateDao bikeStateDao;
	private final WeatherDao weatherDao;

	@Autowired
	public SampleDao(SampleRepository sampleRepository,
	                 BikeStateDao bikeStateDao,
	                 WeatherDao weatherDao) {

		this.sampleRepository = sampleRepository;
		this.bikeStateDao = bikeStateDao;
		this.weatherDao = weatherDao;
	}

	public Sample create() {
		return new Sample();
	}

	@Transactional
	public Optional<Sample> getPrevious() {
		return sampleRepository.findFirstByOrderByTimestampDesc();
	}

	@Transactional
	public List<Sample> getPrevious(int count){
		Page<Sample> page = sampleRepository.findAll(
				PageRequest.of(0, count, Sort.by(Sort.Order.desc("id"))));
		return page.getContent();
	}

	@Transactional
	public Sample save(Sample sample) {
		log.info("Starting sample persisting...");
		Sample previousSample = getPrevious().orElse(new Sample());

		sample = bikeStateDao.persistSample(sample, previousSample);
		sample = weatherDao.persistSample(sample, previousSample);

		Duration creationDuration = Duration.between(sample.getTimestamp(), Instant.now());
		sample.setCreationDuration(creationDuration);
		sample = sampleRepository.save(sample);
		log.info("Saved new sample to the database with id: {}. Operation running time: {}h {}m {}.{}s",
				sample.getId(),
				sample.getCreationDuration().toHoursPart(),
				sample.getCreationDuration().toMinutesPart(),
				sample.getCreationDuration().toSecondsPart(),
				sample.getCreationDuration().toMillisPart());

		return sample;
	}
}
