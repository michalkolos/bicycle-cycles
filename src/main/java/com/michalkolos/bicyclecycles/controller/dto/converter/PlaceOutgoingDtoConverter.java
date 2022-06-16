package com.michalkolos.bicyclecycles.controller.dto.converter;

import com.michalkolos.bicyclecycles.controller.dto.PlaceOutgoingDto;
import com.michalkolos.bicyclecycles.entity.Place;

public class PlaceOutgoingDtoConverter {
	public static PlaceOutgoingDto convert(Place place) {
		return PlaceOutgoingDto.builder()
				.id(place.getId())
				.name(place.getName())
				.position(place.getPosition().toText())
				.rackNo(place.getRackNo())
				.build();
	}
}
