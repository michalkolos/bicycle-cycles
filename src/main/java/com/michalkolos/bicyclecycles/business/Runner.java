/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business;

import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDataService;
import com.michalkolos.bicyclecycles.business.service.nextbike.NextbikeDownloadService;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.MarkersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Runner implements CommandLineRunner {

	private final NextbikeDownloadService nextbikeDownloadService;
	private final NextbikeDataService nextbikeDataService;


	@Autowired
	public Runner(NextbikeDownloadService nextbikeDownloadService, NextbikeDataService nextbikeDataService) {
		this.nextbikeDownloadService = nextbikeDownloadService;
		this.nextbikeDataService = nextbikeDataService;
	}
	

	@Override
	public void run(String... args) throws Exception {

		nextbikeDownloadService.getBikeData()
				.map(nextbikeDataService::ingest);

		Optional<MarkersDto> bikeData = nextbikeDownloadService.getBikeData();



		bikeData.ifPresent(System.out::println);

	}
}
