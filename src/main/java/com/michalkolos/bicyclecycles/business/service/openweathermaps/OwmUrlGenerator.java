/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.openweathermaps;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpRequest;

@Component
public class OwmUrlGenerator {

	public static final String OWM_API_URI =
			"https://api.openweathermap.org/data/2.5/";

	public static final String OWM_API_KEY = "216353d6d8f732836dc5f5cd45404903";


	public HttpRequest get(Point coords) {
		return HttpRequest.newBuilder()
				.uri(URI.create(OWM_API_URI + "weather?appid=" + OWM_API_KEY +
						"&lat=" + coords.getCoordinate().getY() +
						"&lon=" + coords.getCoordinate().getX()))
				.GET()
				.build();
	}

}
