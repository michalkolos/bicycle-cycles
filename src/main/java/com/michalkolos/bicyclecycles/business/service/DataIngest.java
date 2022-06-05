/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service;

import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeAcquisitionService;
import com.michalkolos.bicyclecycles.business.service.weather.OwmWeatherAcquisitionService;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.dao.SampleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class DataIngest {

	private final NextbikeAcquisitionService nextbikeService;
	private final OwmWeatherAcquisitionService owmWeatherAcquisitionService;
	private final SampleDao sampleDao;


	@Autowired
	public DataIngest(NextbikeAcquisitionService nextbikeService,
	                  OwmWeatherAcquisitionService owmWeatherAcquisitionService,
	                  SampleDao sampleDao) {

		this.nextbikeService = nextbikeService;
		this.owmWeatherAcquisitionService = owmWeatherAcquisitionService;
		this.sampleDao = sampleDao;
	}

	public Sample run() {
		Instant taskStartTime = Instant.now();

		Sample sample = sampleDao.create();

		sample = nextbikeService.downloadLatest(sample);
		sample = owmWeatherAcquisitionService.downloadLatest(sample);

		return sampleDao.save(sample);
	}

}
