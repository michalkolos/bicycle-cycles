/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.repository.SampleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class BikeStateDao {

	private final PlaceDao placeDao;
	private final BikeDao bikeDao;
	private final SampleDao sampleDao;

	private final SampleRepository sampleRepository;

	private Map<Bike, BikeState> previousStates = new ConcurrentHashMap<>();

	@Autowired
	public BikeStateDao(PlaceDao placeDao,
	                    BikeDao bikeDao,
	                    SampleDao sampleDao,
	                    SampleRepository sampleRepository) {

		this.placeDao = placeDao;
		this.bikeDao = bikeDao;
		this.sampleDao = sampleDao;
		this.sampleRepository = sampleRepository;
	}


	@Transactional
	public Sample persistSample(Sample sample) {
		log.info("Starting persisting new bike states...");
		placeDao.initTransaction();
		bikeDao.initTransaction();
		uploadPreviousState();

		long totalPreviousStatesCount = previousStates.size();

		Set<BikeState> currentStates = sample.getBikeStates().stream()
				.map(unsynced ->
					Optional.ofNullable(previousStates.remove(unsynced.getBike()))
							.map(previous -> choseCurrentState(previous, unsynced))
							.orElseGet(() -> setStateForSyncing(unsynced)))
				.collect(Collectors.toSet());

		long unchangedStatesCount = currentStates.stream()
						.filter(state -> state.getId() != null)
						.count();
		long totalCurrentStatesCount = currentStates.size();

		sample.setBikeStates(currentStates);
		sample = sampleRepository.save(sample);
		previousStates = generateStateMap(sample.getBikeStates());

		log.info("Persisted {} states: {} new, {} unchanged from previous. Delta vs previous sample: {}",
				totalCurrentStatesCount,
				totalCurrentStatesCount - unchangedStatesCount,
				unchangedStatesCount,
				totalCurrentStatesCount - totalPreviousStatesCount);

		return sample;
	}


	private void uploadPreviousState() {
		log.info("Fetching previous BikeState entities from database...");
		Set<BikeState> previous = sampleDao.getPrevious()
				.map(Sample::getBikeStates)
				.orElse(new HashSet<>());

		previousStates = generateStateMap(previous);

		log.info("Found {} most recent BikeState entities in database", previous.size());
	}

	private Map<Bike, BikeState> generateStateMap(Set<BikeState> stateSet) {
		return  stateSet.stream()
				.collect(Collectors.toMap(BikeState::getBike, state -> state));
	}

	private BikeState choseCurrentState(BikeState previous, BikeState unsynced) {
		return unsynced.equals(previous) ? previous :  setStateForSyncing(unsynced);
	}

	private BikeState setStateForSyncing(BikeState unsynced) {
		unsynced.setPlace(placeDao.sync(unsynced.getPlace()));
		unsynced.setBike(bikeDao.sync(unsynced.getBike()));
		previousStates.put(unsynced.getBike(), unsynced);

		return unsynced;
	}
}

