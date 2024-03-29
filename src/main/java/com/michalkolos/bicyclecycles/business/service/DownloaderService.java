/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@Service
@Log4j2
public class DownloaderService {

	private static final int CONNECTION_TIMEOUT_SECONDS = 60;

	public Optional<String> fromUrl(String urlString, int retries) {

		return Optional.ofNullable(buildClient())
				.flatMap(client -> Optional.ofNullable(buildRequest(urlString))
									.map(request -> sendWithRetries(request, client, retries)));
	}

	private HttpRequest buildRequest (String urlString) {
		try {
			return HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.GET()
					.timeout(Duration.of(CONNECTION_TIMEOUT_SECONDS, ChronoUnit.SECONDS))
					.build();

		} catch (IllegalArgumentException | IllegalStateException e) {
			log.error("Unable to create HTTP request for URL: \"{}\" ({})",
					urlString, e.getMessage());
		}

		return null;
	}

	private HttpClient buildClient() {
		try {
			return HttpClient.newBuilder()
					.connectTimeout(Duration.of(CONNECTION_TIMEOUT_SECONDS, ChronoUnit.SECONDS))
					.build();
		} catch (UncheckedIOException e) {
			log.error("Unable to create HTTP client ({})",
					e.getMessage());
		}

		return null;
	}

	private String sendRequest(HttpRequest request, HttpClient client) {
		try {
			String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

			log.info("Host \"{}\" returned {}B of data",
					request.uri().getHost(),
					response.getBytes(StandardCharsets.UTF_8).length);

			return response;
		} catch (IOException | InterruptedException | IllegalArgumentException
		         | SecurityException e) {

			log.warn("Sending HTTP request to URL: \"{}\" failed ({})",
					request.uri(), e.getMessage());
		}

		return null;
	}

	private String sendWithRetries(HttpRequest request, HttpClient client, int retries) {
		String response = null;
		int counter = 0;
		while(response == null && counter < retries) {
			log.info("Attempting to download data ({}/{}), from URL: {}, with timeout: {}",
					++counter,
					retries,
					request.uri(),
					client.connectTimeout().orElse(Duration.ZERO));

			response = sendRequest(request, client);
		}

		return response;
	}
}
