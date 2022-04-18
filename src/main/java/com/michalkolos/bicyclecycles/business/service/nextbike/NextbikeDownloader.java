package com.michalkolos.bicyclecycles.business.service.nextbike;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Log4j2
public class NextbikeDownloader {
	public static final String NEXTBIKE_API_URI =
			"https://api.nextbike.net/maps/nextbike-official.xml";

	public HttpRequest getNextbikeApiRequest() {
		return HttpRequest.newBuilder()
				.uri(URI.create(NEXTBIKE_API_URI))
				.GET()
				.build();
	}


	private HttpClient createClient() {
		try {

			return HttpClient.newBuilder().build();

		} catch (UncheckedIOException e) {
			log.warn("Unable to create HTTP client, " + e.toString());
		}

		return null;
	}

}
