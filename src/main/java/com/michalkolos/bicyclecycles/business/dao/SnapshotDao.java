/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.dao;

import com.michalkolos.bicyclecycles.business.entity.Snapshot;
import com.michalkolos.bicyclecycles.persistence.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SnapshotDao {

	private final SnapshotRepository snapshotRepository;

	@Autowired
	public SnapshotDao(SnapshotRepository snapshotRepository) {
		this.snapshotRepository = snapshotRepository;
	}

	public Snapshot createSnapshot() {
		return snapshotRepository.save(new Snapshot());
	}
}
