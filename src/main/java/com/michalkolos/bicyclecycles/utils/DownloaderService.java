package com.michalkolos.bicyclecycles.utils;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@Service
@Log4j2
public class DownloaderService {

	public Optional<String> fromUrl(String urlString) {
		String responseBody = null;

		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.GET()
					.build();

			HttpClient client = HttpClient.newBuilder().build();

			responseBody = client.send(
					request, HttpResponse.BodyHandlers.ofString()).body();

		} catch (UncheckedIOException | IOException | InterruptedException e) {
			log.warn("Error while sending request to: " + urlString + ", " + e);
		}

		return Optional.ofNullable(responseBody);
	}
}
