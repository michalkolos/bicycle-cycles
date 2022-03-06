/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CountryDto {

	@JacksonXmlProperty(isAttribute = true)
	private double lat;

	@JacksonXmlProperty(isAttribute = true)
	private double lng;

	@JacksonXmlProperty(isAttribute = true)
	private int zoom;

	@JacksonXmlProperty(isAttribute = true)
	private String name;

	@JacksonXmlProperty(isAttribute = true)
	private String hotline;

	@JacksonXmlProperty(isAttribute = true)
	private String domain;

	@JacksonXmlProperty(isAttribute = true)
	private String language;

	@JacksonXmlProperty(isAttribute = true)
	private String email;

	@JacksonXmlProperty(isAttribute = true)
	private String currency;

	@JacksonXmlProperty(isAttribute = true)
	private String country_calling_code;

	@JacksonXmlProperty(isAttribute = true)
	private String system_operator_address;

	@JacksonXmlProperty(isAttribute = true)
	private String terms;

	@JacksonXmlProperty(isAttribute = true)
	private String policy;

	@JacksonXmlProperty(isAttribute = true)
	private String website;

	@JacksonXmlProperty(isAttribute = true)
	private String booked_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private String set_point_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private String available_bikes;

	@JacksonXmlProperty(isAttribute = true)
	private String pricing;

	@JacksonXmlProperty(isAttribute = true)
	private String faq_url;

	@JacksonXmlProperty(isAttribute = true)
	private String timezone;

	@JacksonXmlProperty(isAttribute = true)
	private String country;

	@JacksonXmlProperty(isAttribute = true)
	private String country_name;


	private Set<CityDto> city = new HashSet<>();
}
