/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.persistence.repository.PlaceRepository;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;


@Component
public class PlaceDao extends AbstractDao<Place, PlaceRepository>{

	private final CityDao cityDao;


	public PlaceDao(PlaceRepository repository, CityDao cityDao) {
		super(repository, "Place");
		this.cityDao = cityDao;
	}

	@Override
	@Transactional
	public void initTransaction() {
		cityDao.initTransaction();
		super.initTransaction();
	}

	@Override
	public Place sync(Place unsynced) {
		unsynced.setCity(cityDao.sync(unsynced.getCity()));
		return super.sync(unsynced);
	}
}
