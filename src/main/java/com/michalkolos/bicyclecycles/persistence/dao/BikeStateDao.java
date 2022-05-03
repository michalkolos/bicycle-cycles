/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.repository.BikeRepository;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import com.michalkolos.bicyclecycles.persistence.repository.PlaceRepository;
import com.michalkolos.bicyclecycles.persistence.repository.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class BikeStateDao {

	private final PlaceDao placeDao;
	private final BikeDao bikeDao;
	private final SampleDao sampleDao;

	private final CityRepository cityRepository;
	private final PlaceRepository placeRepository;
	private final BikeRepository bikeRepository;
	private final SampleRepository sampleRepository;

	private final Map<Bike, BikeState> entityMap = new ConcurrentHashMap<>();

	@Autowired
	public BikeStateDao(PlaceDao placeDao,
	                    BikeDao bikeDao,
	                    SampleDao sampleDao, CityRepository cityRepository, PlaceRepository placeRepository, BikeRepository bikeRepository, SampleRepository sampleRepository) {

		this.placeDao = placeDao;
		this.bikeDao = bikeDao;
		this.sampleDao = sampleDao;
		this.cityRepository = cityRepository;
		this.placeRepository = placeRepository;
		this.bikeRepository = bikeRepository;
		this.sampleRepository = sampleRepository;
	}

	private void fillOutEntityMap() {
		log.info("Generating in memory BikeState repository...");
		Set<BikeState> previous = sampleDao.getPrevious()
				.map(Sample::getBikeStates)
				.orElse(new HashSet<>());

		entityMap.clear();
		previous.forEach(bikeState -> {
			entityMap.put(bikeState.getBike(), bikeState);
		});

		log.info("Found {} BikeState entities in database", previous.size());
	}

	@Transactional
	public Sample syncAll(Sample sample) {
//		List<Place> allPlaces = placeRepository.findAll();
//		List<Bike> allBikes = bikeRepository.findAll();

		placeDao.initTransaction();
		bikeDao.initTransaction();
		fillOutEntityMap();

		Set<BikeState> toKeep = new HashSet<>();

		for(Iterator<BikeState> iter = sample.getBikeStates().iterator(); iter.hasNext();) {
			BikeState unsynced = iter.next();

			Optional.ofNullable(entityMap.get(unsynced.getBike()))
					.ifPresentOrElse(
							previous -> {
								if(unsynced.equals(previous)) {
									iter.remove();
									toKeep.add(previous);
//									sample.removeBikeState(unsynced);
//									sample.addBikeState(previous);


								} else {
									unsynced.setPlace(placeDao.sync(unsynced.getPlace()));
									unsynced.setBike(bikeDao.sync(unsynced.getBike()));
									entityMap.put(unsynced.getBike(), unsynced);
									toKeep.add(unsynced);
								}
							},
							() -> {

								unsynced.setPlace(placeDao.sync(unsynced.getPlace()));
								unsynced.setBike(bikeDao.sync(unsynced.getBike()));
								entityMap.put(unsynced.getBike(), unsynced);
								toKeep.add(unsynced);
							});
		}

		toKeep.forEach(sample::addBikeState);

		return sampleRepository.save(sample);
	}
}

