/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.repository;

import com.michalkolos.bicyclecycles.business.entity.BikeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeStateRepository extends JpaRepository<BikeState, Long> {
}
