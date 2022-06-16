package com.michalkolos.bicyclecycles.controller;

import com.michalkolos.bicyclecycles.persistence.dao.BikeStateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/time_data")
public class TimeDataController {

	private final BikeStateDao bikeStateDao;

	@Autowired
	public TimeDataController(BikeStateDao bikeStateDao) {
		this.bikeStateDao = bikeStateDao;
	}


	@GetMapping("/place")
	public List<Long> bikesAtPlace(@RequestParam long id,@RequestParam int samples) {

		return new ArrayList<>();
	}

}
