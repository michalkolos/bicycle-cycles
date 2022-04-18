package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;

import java.util.Set;

public interface NextbikeSource {
	public Set<BikeState> get();
}
