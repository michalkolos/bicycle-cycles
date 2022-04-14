/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.dao;

import com.michalkolos.bicyclecycles.business.entity.City;
import com.michalkolos.bicyclecycles.business.entity.Place;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.PlaceDto;
import com.michalkolos.bicyclecycles.persistence.repository.PlaceRepository;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PlaceDao {

	PlaceRepository placeRepository;
	GeometryFactory geometryFactory;

	@Autowired
	public PlaceDao(PlaceRepository placeRepository, GeometryFactory geometryFactory) {
		this.placeRepository = placeRepository;
		this.geometryFactory = geometryFactory;
	}

	@Transactional
	public Place update(PlaceDto dto, City city) {
		return find(dto.getUid()).orElse(create(dto, city));
	}

	@Transactional
	public Place create(PlaceDto dto, City city) {

		Point position = geometryFactory.createPoint(
				new Coordinate(dto.getLng(), dto.getLat()));

		Place place = new Place(dto.getUid(), dto.getNumber(), dto.getName(),
				position, dto.getBike_racks(), city);

		return placeRepository.save(place);
	}

	@Transactional
	public Optional<Place> find(Long uid) {
		return placeRepository.findByUid(uid);
	}

	@Transactional
	public Map<Long, Place> getAll() {
		Map<Long, Place> placeMap = new HashMap<>();
		placeRepository.findAll()
				.forEach((Place place) -> placeMap.put(place.getUid(), place));

		return placeMap;
	}
}
