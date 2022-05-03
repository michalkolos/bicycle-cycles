/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.persistence.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CityDao extends AbstractDao<City, CityRepository>{

	@Autowired
	public CityDao(CityRepository cityRepository) {
		super(cityRepository, "City");
	}
}
