package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.MarkersDto;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Place;
import com.michalkolos.bicyclecycles.business.service.DownloaderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Log4j2
public class FullXmlNextbikeSource implements NextbikeSource{

	public static final String NEXTBIKE_API_URI =
			"https://api.nextbike.net/maps/nextbike-official.xml";
	private static final int RETRIES = 20;

	private final DownloaderService downloaderService;
	private final XmlMapper xmlMapper;
	private final NextbikeDtoToEntityConverter nextbikeDtoToEntityConverter;


	@Autowired
	public FullXmlNextbikeSource(DownloaderService downloaderService,
	                             XmlMapper xmlMapper,
	                             NextbikeDtoToEntityConverter nextbikeDtoToEntityConverter) {

		this.downloaderService = downloaderService;
		this.xmlMapper = xmlMapper;
		this.nextbikeDtoToEntityConverter = nextbikeDtoToEntityConverter;
	}

	public Optional<Set<BikeState>> get() {

		return downloaderService.fromUrl(NEXTBIKE_API_URI, RETRIES)
				.map(this::unmarshal)
				.map(this::convertToEntities);
	}

	private MarkersDto unmarshal(String response) {
		try {
			return xmlMapper.readValue(response, MarkersDto.class);
		} catch (JsonProcessingException e) {
			log.warn("Error unmarshalling response message from Nextbike api, "
					+ e.getMessage());
		}

		return null;
	}

	private Set<BikeState> convertToEntities(MarkersDto dto) {
		Set<BikeState> bikeStates = new HashSet<>();

		dto.getCountry().forEach(countryDto -> {
			countryDto.getCity().forEach(cityDto -> {
				City city = nextbikeDtoToEntityConverter.buildCity(countryDto, cityDto);

				cityDto.getPlace().forEach(placeDto -> {
					Place place = nextbikeDtoToEntityConverter.buildPlace(placeDto, city);

					placeDto.getBike().forEach(bikeDto -> {
						nextbikeDtoToEntityConverter.buildBike(bikeDto).ifPresent(bike -> {
							BikeState bikeState = nextbikeDtoToEntityConverter
									.buildBikeState(bikeDto, bike, place);

							bikeStates.add(bikeState);
						});
					});
				});
			});
		});

		return bikeStates;
	}
}
