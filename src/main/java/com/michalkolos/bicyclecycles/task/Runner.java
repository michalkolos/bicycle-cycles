/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.task;

import com.michalkolos.bicyclecycles.business.service.DataIngest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
public class Runner implements CommandLineRunner {

	private final DataIngest dataIngest;

	@Autowired
	public Runner(DataIngest dataIngest) {
		this.dataIngest = dataIngest;
	}

	@Override
	@Transactional
	public void run(String... args) {

		dataIngest.run();
	}
}
