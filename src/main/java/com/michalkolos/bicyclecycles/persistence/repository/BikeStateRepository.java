/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BikeStateRepository extends JpaRepository<BikeState, Long> {
//	List<BikeState> findAllBySampleAndPlace_Id(Sample sample, long placeId);
//	List<BikeState> findAll();
	List<BikeState> findByPlace_IdAndSamplesIn(long id, Set<Sample> sample);


}
