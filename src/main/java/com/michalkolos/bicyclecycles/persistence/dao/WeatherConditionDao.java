package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.OwmWeatherCondition;
import com.michalkolos.bicyclecycles.persistence.repository.WeatherConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherConditionDao extends
		AbstractDao<OwmWeatherCondition, WeatherConditionRepository>{

	@Autowired
	public WeatherConditionDao(WeatherConditionRepository repository) {
		super(repository, "WeatherCondition");
	}
}
