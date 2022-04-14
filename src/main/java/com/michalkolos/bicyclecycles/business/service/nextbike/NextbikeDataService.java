/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.business.dao.BikeDao;
import com.michalkolos.bicyclecycles.business.dao.CityDao;
import com.michalkolos.bicyclecycles.business.dao.PlaceDao;
import com.michalkolos.bicyclecycles.business.dao.SnapshotDao;
import com.michalkolos.bicyclecycles.business.entity.*;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class NextbikeDataService {

	private final SnapshotDao snapshotDao;
	private final CityDao cityDao;
	private final PlaceDao placeDao;
	private final BikeDao bikeDao;


	Logger logger = LoggerFactory.getLogger(NextbikeDataService.class);

	@Autowired
	public NextbikeDataService(SnapshotDao snapshotDao, CityDao cityDao, PlaceDao placeDao, BikeDao bikeDao) {
		this.snapshotDao = snapshotDao;
		this.cityDao = cityDao;
		this.placeDao = placeDao;
		this.bikeDao = bikeDao;
	}


	@Transactional
	public Snapshot ingest(MarkersDto dto) {
		Snapshot snapshot = snapshotDao.create();

		Map<Long, Bike> bikeMap = bikeDao.getAll();
		Map<Long, Place> placeMap = placeDao.getAll();
		Map<Long, City> cityMap = cityDao.getAll();

		for(CountryDto countryDto : dto.getCountry()) {
			for(CityDto cityDto : countryDto.getCity()) {

				City city = cityMap.get(cityDto.getUid());
				if(city == null) {
					city = cityDao.create(cityDto, countryDto);
				}

				CityStats stats = new CityStats(
						cityDto.getBooked_bikes(),
						cityDto.getSet_point_bikes(),
						cityDto.getAvailable_bikes());

				snapshot.addCityStats(stats);
				city.addStats(stats);

				int bikeCounter = 0;
				for(PlaceDto placeDto : cityDto.getPlace()) {

					Place place = placeMap.get(placeDto.getUid());
					if(place == null) {
						place = placeDao.create(placeDto, city);
					}

					for(BikeDto bikeDto : placeDto.getBike()) {

						Bike bike = bikeMap.get(bikeDto.getNumber());
							if(bike == null) {
								bike = bikeDao.create(bikeDto);
							}




						BikeState bikeState = new BikeState(
								bikeDto.getActive() != 0,
								bikeDto.getState(),
								bikeDto.getElectric_lock() != 0,
								bikeDto.getPedelec_battery(),
								place);

						bike.addState(bikeState);
						snapshot.addBikeState(bikeState);

						bikeCounter++;
//						System.out.println(bike);
					}
				}
				logger.info(String.format("Ingested data for city %s, found %d bikes.", city.getName(), bikeCounter));

			}
		}

		logger.info("Updated data for " + snapshot.getBikeStatesList().size() + " bikes.");
		return snapshotDao.save(snapshot);
	}

}
