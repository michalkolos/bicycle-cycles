package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@Slf4j
public class NextbikeAcquisitionService {

	private final NextbikeSource nextbikeSource;

	@Autowired
	public NextbikeAcquisitionService(NextbikeSource nextbikeSource) {
		this.nextbikeSource = nextbikeSource;
	}

	public Sample downloadLatest(final Sample sample) {
		Set<BikeState> bikeStates = nextbikeSource.get()
				.orElse(Collections.emptySet());

		sample.setBikeStates(bikeStates);

		return sample;
	}
}
