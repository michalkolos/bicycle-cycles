/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;


@Component
@Slf4j
public class CityDao extends AbstractDao<City, CityRepository>{

	@Autowired
	public CityDao(CityRepository cityRepository) {
		super(cityRepository, "City");
	}

	@Transactional
	public City saveUpdatedOwmId(City withId) {
		return Optional.ofNullable(withId)
						.map(City::getUid)
						.flatMap(repository::findByUid)
						.map(stored -> applyOwmIdToStoredCity(stored, withId))
						.map(repository::save)
						.orElseGet(() -> logFailure(withId));

	}

	private City applyOwmIdToStoredCity(City stored, City withId) {
		if(stored.getOwmId().equals(withId.getOwmId())) {
			log.info("City's: {} OWM ID is up to date",
					stored.getName());

		}else if (stored.getOwmId() != 0) {
			log.info("City: {} has stored OWM ID {}, changing to {}.",
					stored.getName(),
					stored.getOwmId(),
					withId.getOwmId());
		} else {
			log.info("City {} has no OWM ID stored, setting to {}.",
					stored.getName(),
					withId.getOwmId());
		}

		stored.setOwmId(withId.getOwmId());
		return stored;
	}

	private City logFailure(City withId) {
		String cityName = Optional.ofNullable(withId)
				.map(City::getName)
				.orElse("NULL");
		log.info("Unable to set OWM ID for {}.",
				cityName);
		return withId;
	}

}
