/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.business.dao.CityDao;
import com.michalkolos.bicyclecycles.business.dao.SnapshotDao;
import com.michalkolos.bicyclecycles.business.entity.City;
import com.michalkolos.bicyclecycles.business.entity.Snapshot;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.MarkersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class NextbikeDataService {

	private final SnapshotDao snapshotDao;
	private final CityDao cityDao;

	@Autowired
	public NextbikeDataService(SnapshotDao snapshotDao, CityDao cityDao) {
		this.snapshotDao = snapshotDao;
		this.cityDao = cityDao;
	}

	@Transactional
	public Set<City> ingest(MarkersDto incomingData) {
		Snapshot snapshot = snapshotDao.createSnapshot();

		Set<City> cities = new HashSet<>();

		incomingData.getCountry().forEach(countryDto -> {
			countryDto.getCity().forEach(cityDto -> {
				cities.add(cityDao.updateCityStats(snapshot, countryDto, cityDto));
			});
		});

		cityDao.saveChanges(cities);

		return cities;
	}

	private Set<City> updateCities(MarkersDto incomingData, Snapshot snapshot) {




	}
}
