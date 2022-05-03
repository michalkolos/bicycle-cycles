package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.entity.BikeState;

import java.util.Optional;
import java.util.Set;

public interface NextbikeSource {
	public Optional<Set<BikeState>> get();
}
