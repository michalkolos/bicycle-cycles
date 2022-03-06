/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.configuration;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class CityBoundsDeserializer extends StdDeserializer<Geometry> {


	protected CityBoundsDeserializer() {
		super(Geometry.class);
	}

	@Override
	public Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

		JsonNode jsonNode = new ObjectMapper().readTree(ctxt.readTree(p).asText());
		double x1 = jsonNode.get("south_west").get("lng").asDouble(0.0);
		double y1 = jsonNode.get("south_west").get("lat").asDouble(0.0);
		double x2 = jsonNode.get("north_east").get("lng").asDouble(0.0);
		double y2 = jsonNode.get("north_east").get("lat").asDouble(0.0);

		Envelope envelope = new Envelope(new Coordinate(x1, y1), new Coordinate(x2, y2));

		return new GeometryFactory().toGeometry(envelope);
	}


//	private Envelope parseBounds(String jsonString) throws JsonProcessingException {
//		JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
//		double x1 = jsonNode.get("south_west").get("lng").asDouble(0.0);
//		double y1 = jsonNode.get("south_west").get("lat").asDouble(0.0);
//		double x2 = jsonNode.get("north_east").get("lng").asDouble(0.0);
//		double y2 = jsonNode.get("north_east").get("lat").asDouble(0.0);
//
//		return new Envelope(new Coordinate(x1, y1), new Coordinate(x2, y2));
//	}
}
