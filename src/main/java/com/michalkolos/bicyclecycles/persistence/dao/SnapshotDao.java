/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.Snapshot;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.persistence.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class SnapshotDao {

	private final SnapshotRepository snapshotRepository;

	@Autowired
	public SnapshotDao(SnapshotRepository snapshotRepository) {
		this.snapshotRepository = snapshotRepository;
	}

	public Snapshot create() {
		return snapshotRepository.save(new Snapshot());
	}

	@Transactional
	public Snapshot save(Snapshot snapshot) {
//		Optional<Snapshot> previous = snapshotRepository.findFirstOrderByTimestampDesc();
//		snapshotRepository.findFirstOrderByTimestampDesc()
//				.ifPresent(previous -> {
//					previous.w
//				})

//		previous.ifPresent();


//		return snapshotRepository.save(snapshot);

		return snapshot;
	}

	@Transactional
	public Set<Snapshot> saveAll (Collection<Snapshot> snapshots) {
		Set<Weather> previousWeather = getLast()
				.map(Snapshot::getWeathers)
				.orElse(new HashSet<>());

		return new HashSet<>();
	}

	public Optional<Snapshot> getLast() {
		return snapshotRepository.findFirstByOrderByTimestampDesc();
	}
}
