package com.michalkolos.bicyclecycles.controller;

import com.michalkolos.bicyclecycles.controller.dto.CityOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.PlaceOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.converter.CityOutgoingDtoConverter;
import com.michalkolos.bicyclecycles.controller.dto.converter.PlaceOutgoingDtoConverter;
import com.michalkolos.bicyclecycles.persistence.dao.CityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/city")
public class CityController {

	private final CityDao cityDao;

	@Autowired
	public CityController(CityDao cityDao) {
		this.cityDao = cityDao;
	}

	@GetMapping("/")
	public List<CityOutgoingDto> getAllCityList() {

		return cityDao.getAll().stream()
				.peek(city-> city.setPlaces(null))
				.map(CityOutgoingDtoConverter::convert)
				.toList();
	}

	@GetMapping("/test")
	public String testEndpoint() {
		return "Hello from test endpoint!";
	}

	@GetMapping("/places")
	public List<PlaceOutgoingDto> getCityPlaces(@RequestParam long id) {

		return cityDao.getCityPlacesById(id).stream()
				.map(PlaceOutgoingDtoConverter::convert)
				.toList();
	}
}
