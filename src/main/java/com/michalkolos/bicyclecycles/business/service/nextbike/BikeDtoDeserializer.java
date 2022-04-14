/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.BikeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BikeDtoDeserializer extends StdDeserializer<BikeDto> {

	private static final Logger log = LoggerFactory.getLogger(BikeDtoDeserializer.class);

	public BikeDtoDeserializer() {
		super(BikeDtoDeserializer.class);
	}

	@Override
	public BikeDto deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException {

		JsonNode jsonNode = ctxt.readTree(p);
		BikeDto bikedto = new BikeDto();

		if(jsonNode.isValueNode()) { return new BikeDto(); }

		try {
			bikedto.setNumber(jsonNode.get("number").asLong());
			bikedto.setBike_type(jsonNode.get("bike_type").asInt());
			bikedto.setActive(jsonNode.get("active").asInt());

			if(jsonNode.has("state")) {
				bikedto.setState(jsonNode.get("state").asText());
			} else {
				bikedto.setState("UNDEFINED");
			}

			bikedto.setLock_types(jsonNode.get("lock_types").asText());

			if(jsonNode.has("boardcomputer")) {
				bikedto.setBoardcomputer(jsonNode.get("boardcomputer").asLong(0));
			}

			if(jsonNode.has("electric_lock")) {
				bikedto.setElectric_lock(jsonNode.get("electric_lock").asInt(0));
			}

			if(jsonNode.has("pedelec_battery")) {
				String batteryString = jsonNode.get("pedelec_battery").asText();

				if(!(batteryString.isBlank() || batteryString.equals("\"\""))) {
					bikedto.setElectric(true);
					bikedto.setPedelec_battery(
							jsonNode.get("pedelec_battery").asInt());
				}
			}

		}catch (NullPointerException e){
			log.error("Unable to parse bike data: " + jsonNode.toPrettyString());
		}


		return bikedto;
	}
}
