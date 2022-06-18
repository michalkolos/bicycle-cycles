package com.michalkolos.bicyclecycles.controller.dto.converter;

import com.michalkolos.bicyclecycles.controller.dto.PlaceBikesOutgoingDto;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.Place;

import java.util.List;

public class PlaceBikesOutgoingDtoConverter {
	public static PlaceBikesOutgoingDto convert(
			Long placeId, List<BikeState> bikeStates) {

		return PlaceBikesOutgoingDto.builder()
				.placeId(placeId)
				.bikeIds(bikeStates.stream().map(BikeState::getId).toList())
				.build();
	}

	public static PlaceBikesOutgoingDto convert(
			Place place, List<BikeState> bikeStates) {

		return convert(place.getId(), bikeStates);
	}
}
