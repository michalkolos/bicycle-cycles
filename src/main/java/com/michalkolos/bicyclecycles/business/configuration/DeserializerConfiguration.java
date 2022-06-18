/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.michalkolos.bicyclecycles.business.service.nextbike.BikeDtoDeserializer;
import com.michalkolos.bicyclecycles.business.service.nextbike.CityBoundsDeserializer;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.BikeDto;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.OwmCityDeserializer;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeserializerConfiguration {

	@Bean
	@Qualifier("xmlMapper")
	public XmlMapper xmlMapper() {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		module.addDeserializer(BikeDto.class, new BikeDtoDeserializer());
		module.addDeserializer(Geometry.class, new CityBoundsDeserializer());

		XmlMapper xmlMapper = new XmlMapper(module);

		xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

		return xmlMapper;
	}

	@Bean
	@Qualifier("objectMapper")
	public ObjectMapper objectMapper(){
		SimpleModule module = new SimpleModule();
		module.addDeserializer(OwmCityDto.class, new OwmCityDeserializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);
		objectMapper.registerModule(new JavaTimeModule());

		return objectMapper;
	}

}
