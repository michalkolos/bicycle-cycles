/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.http.HttpRequest;

@Configuration
public class NextbikeApiConfiguration {
	public static final String NEXTBIKE_API_URI =
			"https://api.nextbike.net/maps/nextbike-official.xml";

	@Bean
	public HttpRequest getNextbikeApiRequest() {
		return HttpRequest.newBuilder()
				.uri(URI.create(NEXTBIKE_API_URI))
				.GET()
				.build();
	}
}
