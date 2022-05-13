/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business;

import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeAcquisitionService;
import com.michalkolos.bicyclecycles.business.service.weather.WeatherAcquisitionService;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.dao.SampleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class Runner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	private final NextbikeAcquisitionService nextbikeService;
	private final WeatherAcquisitionService weatherAcquisitionService;
	private final SampleDao sampleDao;


	@Autowired
	public Runner(NextbikeAcquisitionService nextbikeService, WeatherAcquisitionService weatherAcquisitionService, SampleDao sampleDao) {
		this.nextbikeService = nextbikeService;
		this.weatherAcquisitionService = weatherAcquisitionService;
		this.sampleDao = sampleDao;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

//		Optional<Snapshot> snapshot = nextbikeDownloadService.getBikeData().map(nextbikeDataService::ingest);

		Sample sample = sampleDao.create();

		sample = nextbikeService.downloadCurrent(sample);
//		weatherService.downloadCurrent(sample);

		logger.info("Finished ingesting data.");
	}
}
