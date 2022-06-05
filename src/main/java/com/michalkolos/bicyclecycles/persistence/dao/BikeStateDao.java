/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class BikeStateDao {

	private final PlaceDao placeDao;
	private final BikeDao bikeDao;

	@Autowired
	public BikeStateDao(PlaceDao placeDao,
	                    BikeDao bikeDao) {

		this.placeDao = placeDao;
		this.bikeDao = bikeDao;
	}


	@Transactional
	public Sample persistSample(Sample sample, Sample previousSample) {
		log.info("Starting persisting new bike states...");
		placeDao.initTransaction();
		bikeDao.initTransaction();

		final Map<Bike, BikeState> previousStates = generateStateMap(previousSample.getBikeStates());
		long totalPreviousStatesCount = previousStates.size();
		log.info("Found {} most recent BikeState entities in database", totalPreviousStatesCount);
		Set<BikeState> currentStates = sample.getBikeStates().stream()
				.map(unsynced ->
					Optional.ofNullable(previousStates.remove(unsynced.getBike()))
							.map(previous -> choseCurrentState(previous, unsynced, previousStates))
							.orElseGet(() -> setStateForSyncing(unsynced, previousStates)))
				.collect(Collectors.toSet());

		long unchangedStatesCount = currentStates.stream()
						.filter(state -> state.getId() != null)
						.count();
		long totalCurrentStatesCount = currentStates.size();

		sample.setBikeStates(currentStates);

		log.info("Persisted {} states: {} new, {} unchanged from previous. Delta vs previous sample: {}",
				totalCurrentStatesCount,
				totalCurrentStatesCount - unchangedStatesCount,
				unchangedStatesCount,
				totalCurrentStatesCount - totalPreviousStatesCount);

		return sample;
	}
	private Map<Bike, BikeState> generateStateMap(Set<BikeState> stateSet) {
		return  stateSet.stream()
				.collect(Collectors.toMap(BikeState::getBike, state -> state));
	}

	private BikeState choseCurrentState(BikeState previous,
	                                    BikeState unsynced,
	                                    Map<Bike, BikeState> previousStates) {

		return unsynced.equals(previous) ? previous :  setStateForSyncing(unsynced, previousStates);
	}

	private BikeState setStateForSyncing(BikeState unsynced,
	                                     Map<Bike, BikeState> previousStates) {

		unsynced.setPlace(placeDao.sync(unsynced.getPlace()));
		unsynced.setBike(bikeDao.sync(unsynced.getBike()));
		previousStates.put(unsynced.getBike(), unsynced);

		return unsynced;
	}
}

