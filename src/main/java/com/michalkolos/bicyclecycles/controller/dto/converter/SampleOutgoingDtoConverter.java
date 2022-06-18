package com.michalkolos.bicyclecycles.controller.dto.converter;

import com.michalkolos.bicyclecycles.controller.dto.PlaceBikesOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.SampleOutgoingDto;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.entity.Sample;

import java.util.List;
import java.util.Map;

public class SampleOutgoingDtoConverter {
	public static SampleOutgoingDto convert(Sample sample,
	                                        List<PlaceBikesOutgoingDto> places) {

		return SampleOutgoingDto.builder()
				.id(sample.getId())
				.timestamp(sample.getTimestamp())
				.places(places)
				.build();
	}

	public static SampleOutgoingDto convert(Sample sample) {

		return SampleOutgoingDto.builder()
				.id(sample.getId())
				.timestamp(sample.getTimestamp())
				.build();
	}
}
