package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.dao.BikeStateDao;
import com.michalkolos.bicyclecycles.persistence.dao.SampleDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class NextbikeService {

	public static final int RETRIES = 5;

	private final NextbikeSource nextbikeSource;
	private final BikeStateDao bikestateDao;

	@Autowired
	public NextbikeService(NextbikeSource nextbikeSource, BikeStateDao bikeStateDao) {
		this.nextbikeSource = nextbikeSource;
		this.bikestateDao = bikeStateDao;
	}

	public Sample downloadCurrent(Sample sample) {
		Optional<Set<BikeState>> bikeStates = nextbikeSource.get();
		int retryCount = 0;

		while(bikeStates.isEmpty() && retryCount++ < RETRIES) {
			log.warn("Logging bike states failed, retrying " + retryCount + "/" + RETRIES + ".");
			bikeStates = nextbikeSource.get();
		}


		bikeStates.ifPresentOrElse(
				states -> {
					sample.setBikeStates(states);
					bikestateDao.syncAll(sample);
				},
				() -> log.error("Unable to log bike states.")
		);

		return sample;
	}



}
