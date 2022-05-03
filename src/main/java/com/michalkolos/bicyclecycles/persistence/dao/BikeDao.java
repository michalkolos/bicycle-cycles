/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.persistence.repository.BikeRepository;
import org.springframework.stereotype.Component;


@Component
public class BikeDao extends AbstractDao<Bike, BikeRepository>{

	public BikeDao(BikeRepository repository) {
		super(repository, "Bike");
	}

}
