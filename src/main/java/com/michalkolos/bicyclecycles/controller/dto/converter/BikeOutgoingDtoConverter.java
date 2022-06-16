package com.michalkolos.bicyclecycles.controller.dto.converter;

import com.michalkolos.bicyclecycles.controller.dto.BikeOutgoingDto;
import com.michalkolos.bicyclecycles.entity.Bike;

public class BikeOutgoingDtoConverter {
	public static BikeOutgoingDto convert(Bike bike) {
		return BikeOutgoingDto.builder()
				.id(bike.getId())
				.number(bike.getNumber())
				.bikeType(bike.getBikeType())
				.lockTypes(bike.getLockTypes())
				.isElectric(bike.isElectric())
				.build();
	}
}
