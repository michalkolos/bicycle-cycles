/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class PlaceDto {

	@JacksonXmlProperty(isAttribute = true)
	private int uid;

	@JacksonXmlProperty(isAttribute = true)
	private double lat;

	@JacksonXmlProperty(isAttribute = true)
	private double lng;

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(isAttribute = true)
	private int spot;

	@JacksonXmlProperty(isAttribute = true)
	private long number;

	@JacksonXmlProperty(isAttribute = true)
	private int booked_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private int bikes;

	@JacksonXmlProperty(isAttribute = true)
	private int bikes_available_to_rent;

	@JacksonXmlProperty(isAttribute = true)
	private int bike_racks;

	@JacksonXmlProperty(isAttribute = true)
	private int free_racks;

	@JacksonXmlProperty(isAttribute = true)
	private String terminal_type;

	@JacksonXmlProperty(isAttribute = true)
	private String bike_numbers;

//  bike_types="{"4":6,"undefined":2}"
	@JacksonXmlProperty(isAttribute = true)
	private String bike_types;

	@JacksonXmlProperty(isAttribute = true)
	private int place_type;


	private Set<BikeDto> bike = new HashSet<>();
}