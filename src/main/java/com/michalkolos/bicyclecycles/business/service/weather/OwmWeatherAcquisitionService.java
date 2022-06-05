/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.entity.OwmWeather;
import com.michalkolos.bicyclecycles.persistence.dao.CityDao;
import com.michalkolos.bicyclecycles.persistence.dao.WeatherDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class OwmWeatherAcquisitionService {

	private final OwmWeatherSource owmWeatherSource;
	private final CityDao cityDao;

	public static final int RETRIES = 5;

	@Autowired
	public OwmWeatherAcquisitionService(OwmWeatherSource owmWeatherSource,
	                                    CityDao cityDao) {

		this.owmWeatherSource = owmWeatherSource;
		this.cityDao = cityDao;
	}


	@Transactional
	public Sample downloadLatest(Sample sample) {

		Set<OwmWeather> owmWeathers = Optional.of(cityDao.getAll())
				.map(owmWeatherSource::get)
				.orElse(Collections.emptySet());

		sample.setOwmWeathers(owmWeathers);

		return sample;
	}
}
