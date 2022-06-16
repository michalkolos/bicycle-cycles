package com.michalkolos.bicyclecycles.controller.dto.converter;

import com.michalkolos.bicyclecycles.controller.dto.CityOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.PlaceOutgoingDto;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Place;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CityOutgoingDtoConverter {

	public static CityOutgoingDto convert(City city) {

		CityOutgoingDto.CityOutgoingDtoBuilder cityBuilder = CityOutgoingDto.builder()
				.id(city.getId())
				.alias(city.getAlias())
				.bounds(city.getBounds().toText())
				.country(city.getCountryName())
				.name(city.getName());

		Optional.ofNullable(city.getPlaces())
				.map(CityOutgoingDtoConverter::convertPlaceList)
				.ifPresent(cityBuilder::places);

		return cityBuilder.build();
	}

	private static List<PlaceOutgoingDto> convertPlaceList(Collection<Place> placeList) {
		return placeList.stream()
				.map(PlaceOutgoingDtoConverter::convert)
				.toList();
	}
}

