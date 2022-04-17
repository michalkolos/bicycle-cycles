/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business;

import com.michalkolos.bicyclecycles.persistence.dao.SnapshotDao;
import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDataService;
import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDownloadService;
import com.michalkolos.bicyclecycles.business.service.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
public class Runner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);

	private final NextbikeDownloadService nextbikeDownloadService;
	private final NextbikeDataService nextbikeDataService;
	private final WeatherService weatherService;
	private final SnapshotDao snapshotDao;


	@Autowired
	public Runner(NextbikeDownloadService nextbikeDownloadService,
	              NextbikeDataService nextbikeDataService,
	              WeatherService weatherService,
	              SnapshotDao snapshotDao) {

		this.nextbikeDownloadService = nextbikeDownloadService;
		this.nextbikeDataService = nextbikeDataService;
		this.weatherService = weatherService;
		this.snapshotDao = snapshotDao;
	}


	@Override
	@Transactional
	public void run(String... args) throws Exception {

//		Optional<Snapshot> snapshot = nextbikeDownloadService.getBikeData().map(nextbikeDataService::ingest);
		weatherService.downloadCurrent(new Snapshot());

		logger.info("Finished ingesting data.");
	}
}
