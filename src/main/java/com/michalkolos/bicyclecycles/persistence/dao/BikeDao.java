/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.*;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.*;
import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.persistence.repository.BikeRepository;
import com.michalkolos.bicyclecycles.persistence.repository.BikeStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BikeDao {

	private final BikeRepository bikeRepository;
	private final BikeStateRepository bikeStateRepository;


	@Autowired
	public BikeDao(BikeRepository bikeRepository,
	               BikeStateRepository bikeStateRepository) {

		this.bikeRepository = bikeRepository;
		this.bikeStateRepository = bikeStateRepository;
	}



	@Transactional
	public Bike update(Snapshot snapshot, Place place, BikeDto dto) {
		Bike bike = find(dto.getNumber()).orElse(create(dto));

		BikeState bikeState = new BikeState(
				dto.getActive() != 0,
				dto.getState(),
				dto.getElectric_lock() != 0,
				dto.getPedelec_battery(),
				place);

		bike.addState(bikeState);
		snapshot.addBikeState(bikeState);

		bikeStateRepository.save(bikeState);
		bikeRepository.save(bike);

		return bike;
	}

	@Transactional
	public Optional<Bike> find(Long number) {

		return bikeRepository.findFirstByNumber(number);
	}

	@Transactional
	public Bike create(BikeDto dto) {
		Bike newBike = new Bike(dto.getNumber(), dto.getBike_type(),
				dto.getLock_types(), dto.isElectric());

		newBike = bikeRepository.save(newBike);

		return newBike;
	}

	@Transactional
	public Map<Long, Bike> getAll() {
		Map<Long, Bike> bikeMap = new HashMap<>();
		bikeRepository.findAll()
				.forEach((Bike bike) -> bikeMap.put(bike.getNumber(), bike));

		return bikeMap;
	}
}
