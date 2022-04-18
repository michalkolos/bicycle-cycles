/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;



@Component
public class NextbikeDownloaderDeprecated {

	private static final Logger logger = LoggerFactory.getLogger(NextbikeDownloaderDeprecated.class);

	private final XmlMapper xmlMapper;
	private final HttpRequest nextbikeApiRequest;



	@Autowired
	public NextbikeDownloaderDeprecated(XmlMapper xmlMapper,
	                                    HttpRequest nextbikeApiRequest) {

		this.xmlMapper = xmlMapper;
		this.nextbikeApiRequest = nextbikeApiRequest;
	}



	public Optional<MarkersDto> getBikeData() {

		return Optional.ofNullable(createClient())
				.map(this::sendRequest)
				.map(this::parseResponse);
	}



	private HttpClient createClient() {
		try {

			return HttpClient.newBuilder().build();

		} catch (UncheckedIOException e) {
			logger.warn("Unable to create HTTP client, " + e.toString());
		}

		return null;
	}

	private String sendRequest(HttpClient client) {
		try {

			return client.send(
					nextbikeApiRequest, HttpResponse.BodyHandlers.ofString()).body();

		} catch (IOException | InterruptedException e) {
			logger.warn("Error sending HTTP request, " + e.getMessage());
		}
		return null;
	}

	private MarkersDto parseResponse(String responseBody) {
		try {

			return xmlMapper.readValue(responseBody, MarkersDto.class);



		} catch (JsonProcessingException e) {
			logger.warn("Error parsing response message, " + e.getMessage());
		}
		return null;
	}
}
