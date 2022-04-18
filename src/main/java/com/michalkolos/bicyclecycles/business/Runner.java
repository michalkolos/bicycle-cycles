/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business;

import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeService;
import com.michalkolos.bicyclecycles.persistence.dao.SnapshotDao;
import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDataService;
import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDownloaderDeprecated;
import com.michalkolos.bicyclecycles.business.service.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class Runner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	private final NextbikeService nextbikeService;
	private final WeatherService weatherService;


	@Autowired
	public Runner(NextbikeService nextbikeService, WeatherService weatherService) {
		this.nextbikeService = nextbikeService;
		this.weatherService = weatherService;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

//		Optional<Snapshot> snapshot = nextbikeDownloadService.getBikeData().map(nextbikeDataService::ingest);

		nextbikeService.downloadCurrent(new Snapshot());
//		weatherService.downloadCurrent(new Snapshot());

		logger.info("Finished ingesting data.");
	}
}
