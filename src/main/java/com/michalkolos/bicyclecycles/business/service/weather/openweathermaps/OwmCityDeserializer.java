/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto.OwmCityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;


public class OwmCityDeserializer extends StdDeserializer<OwmCityDto> {

	private static final Logger log = LoggerFactory.getLogger(OwmCityDeserializer.class);

	public OwmCityDeserializer() {
		super(OwmCityDeserializer.class);
	}

	@Override
	public OwmCityDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

		JsonNode jsonNode = ctxt.readTree(p);
		OwmCityDto dto = new OwmCityDto();

		try {
			dto.setId(jsonNode.get("id").asLong());
			dto.setName(jsonNode.get("name").asText());
			dto.setCalculatedTime(Instant.ofEpochSecond(jsonNode.get("dt").asLong()));
			dto.setClouds(jsonNode.get("clouds").get("all").asInt());
			dto.setCondition_id(jsonNode.get("weather").get(0).get("id").asInt());
			dto.setDescription(jsonNode.get("weather").get(0).get("description").asText());
			dto.setHuman_temp(jsonNode.get("main").get("feels_like").asDouble());
			dto.setHumidity(jsonNode.get("main").get("humidity").asInt());
			dto.setPressure(jsonNode.get("main").get("pressure").asInt());
			dto.setTemp(jsonNode.get("main").get("temp").asDouble());
			dto.setVisibility(jsonNode.get("visibility").asInt());
			dto.setWind_deg(jsonNode.get("wind").get("deg").asInt());
			dto.setWind_speed(jsonNode.get("wind").get("speed").asDouble());

			if(jsonNode.has("rain")){
				JsonNode rainNode = jsonNode.get("rain");
				if(rainNode.has("1h")) {
					dto.setRain(rainNode.get("1h").asDouble());
				} else if(rainNode.has("3h")) {
					dto.setRain(rainNode.get("3h").asDouble() / 3);
				}

			}

			if(jsonNode.has("snow")){
				JsonNode snowNode = jsonNode.get("snow");
				if(snowNode.has("1h")) {
					dto.setSnow(snowNode.get("1h").asDouble());
				} else if(snowNode.has("3h")) {
					dto.setSnow(snowNode.get("3h").asDouble() / 3);
				}
			}

			dto.setSunrise(Instant.ofEpochSecond(jsonNode.get("sys").get("sunrise").asLong()));
			dto.setSunset(Instant.ofEpochSecond(jsonNode.get("sys").get("sunset").asLong()));

		} catch (NullPointerException e) {
			log.error("Error parsing weather data.");
		}


		return dto;
	}
}
