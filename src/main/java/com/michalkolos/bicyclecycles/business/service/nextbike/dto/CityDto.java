/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;



@Data
public class CityDto {

	@JacksonXmlProperty(isAttribute = true)
	private long uid;

	@JacksonXmlProperty(isAttribute = true)
	private double lat;

	@JacksonXmlProperty(isAttribute = true)
	private double lng;

	@JacksonXmlProperty(isAttribute = true)
	private int zoom;

	@JacksonXmlProperty(isAttribute = true)
	private String alias;

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(isAttribute = true)
	private int refresh_rate;

	@JacksonXmlProperty(isAttribute = true)
	private Geometry bounds;

	@JacksonXmlProperty(isAttribute = true)
	private int booked_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private int set_point_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private int available_bikes;

	private List<PlaceDto> place = new ArrayList<>();

}


