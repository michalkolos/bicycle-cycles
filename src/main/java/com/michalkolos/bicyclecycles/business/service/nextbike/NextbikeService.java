package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.entity.Snapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NextbikeService {

	private final NextbikeSource nextbikeSource;

	@Autowired
	public NextbikeService(NextbikeSource nextbikeSource) {
		this.nextbikeSource = nextbikeSource;
	}

	public void downloadCurrent(Snapshot snapshot) {
		snapshot.setBikeStates(nextbikeSource.get());

		System.out.println();
	}

}
