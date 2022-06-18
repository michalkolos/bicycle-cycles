package com.michalkolos.bicyclecycles.controller;

import com.michalkolos.bicyclecycles.controller.dto.PlaceBikesOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.SampleOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.converter.PlaceBikesOutgoingDtoConverter;
import com.michalkolos.bicyclecycles.controller.dto.converter.SampleOutgoingDtoConverter;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.entity.Sample;
import com.michalkolos.bicyclecycles.persistence.dao.BikeStateDao;
import com.michalkolos.bicyclecycles.persistence.dao.PlaceDao;
import com.michalkolos.bicyclecycles.persistence.dao.SampleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/time_data")
public class TimeDataController {

	private final BikeStateDao bikeStateDao;
	private final SampleDao sampleDao;
	private final PlaceDao placeDao;


	@Autowired
	public TimeDataController(BikeStateDao bikeStateDao, SampleDao sampleDao, PlaceDao placeDao) {
		this.bikeStateDao = bikeStateDao;
		this.sampleDao = sampleDao;
		this.placeDao = placeDao;
	}

	@GetMapping("/place")
	public List<SampleOutgoingDto> bikesAtPlace(@RequestParam long id,@RequestParam int sampleNo) {

		return sampleDao.getPrevious(sampleNo).stream()
				.map(sample -> makeSampleDto(sample, List.of(id)))
				.toList();
	}

	@GetMapping("/city")
	public List<SampleOutgoingDto> bikesAtCity(@RequestParam long id,@RequestParam int sampleNo) {

		List<Long> placeIds = placeDao.getCityPlaces(id).stream()
				.map(Place::getId)
				.toList();

		return sampleDao.getPrevious(sampleNo).stream()
				.map(sample -> makeSampleDto(sample, placeIds))
				.toList();
	}

	private SampleOutgoingDto makeSampleDto(Sample sample, List<Long> placeIdList) {

		List<PlaceBikesOutgoingDto> placeDtoList = placeIdList.stream()
				.map(placeId -> {
					List<BikeState> bikeStates = bikeStateDao
							.getBikeStatesAtPlaceForSample(placeId, sample);
					return PlaceBikesOutgoingDtoConverter.convert(placeId, bikeStates);
				})
				.toList();

		return SampleOutgoingDtoConverter.convert(sample, placeDtoList);
	}
}
