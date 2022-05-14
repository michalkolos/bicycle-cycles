/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.persistence.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractDao<T, R extends JpaRepository<T, Long>> {

	protected final R repository;
	protected String entityName;
	private final Map<Integer, T> entityMap = new ConcurrentHashMap<>();

	public AbstractDao(R repository, String entityName) {
		this.repository = repository;
		this.entityName = entityName;
	}


	@Transactional
	public void initTransaction() {
		fillOutEntityMap();
	}

	@Transactional
	public List<T> getAll() {
		return repository.findAll();
	}

	private void fillOutEntityMap() {
		log.info("Fetching previous {} entities from database...", entityName);
		List<T> allEntities = getAll();
		entityMap.clear();
		allEntities.forEach(entity -> entityMap.put(entity.hashCode(),entity));
		log.info("Found {} {} entities in database", allEntities.size(), entityName);
	}

	@Transactional
	public T sync(T notSynced) {
		return Optional.ofNullable(entityMap.get(notSynced.hashCode()))
				.orElseGet(() -> {
//					T synced = repository.save(notSynced);
					entityMap.put(notSynced.hashCode(), notSynced);
					log.info("Received new {} entity: {}", entityName, notSynced);
					return notSynced;
				});
	}


}
