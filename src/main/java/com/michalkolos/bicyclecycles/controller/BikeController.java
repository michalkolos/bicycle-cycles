package com.michalkolos.bicyclecycles.controller;

import com.michalkolos.bicyclecycles.controller.dto.BikeOutgoingDto;
import com.michalkolos.bicyclecycles.controller.dto.converter.BikeOutgoingDtoConverter;
import com.michalkolos.bicyclecycles.persistence.dao.BikeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/bike")
public class BikeController {
	private final BikeDao bikeDao;

	@Autowired
	public BikeController(BikeDao bikeDao) {
		this.bikeDao = bikeDao;
	}

	@GetMapping("/")
	public Optional<BikeOutgoingDto> getBikeById(@RequestParam  long id) {
		return bikeDao.getById(id)
				.map(BikeOutgoingDtoConverter::convert);
	}
}
