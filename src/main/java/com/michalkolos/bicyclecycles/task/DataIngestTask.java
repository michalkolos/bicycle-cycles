/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.task;

import com.michalkolos.bicyclecycles.business.service.DataIngest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataIngestTask {
	DataIngest dataIngest;

	@Autowired
	public DataIngestTask(DataIngest dataIngest) {
		this.dataIngest = dataIngest;
	}

	//  Running every 10 minutes starting from full hour
	@Scheduled(cron = "0 0/10 * * * *")
	public void run() {
		log.info("Starting data ingestion task.");
		dataIngest.run();
		log.info("Data ingestion task finished.");
	}
}
