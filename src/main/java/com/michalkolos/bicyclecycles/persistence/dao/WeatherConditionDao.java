package com.michalkolos.bicyclecycles.persistence.dao;

import com.michalkolos.bicyclecycles.entity.WeatherCondition;
import com.michalkolos.bicyclecycles.persistence.repository.WeatherConditionRepository;
import org.springframework.stereotype.Component;

@Component
public class WeatherConditionDao extends
		AbstractDao<WeatherCondition, WeatherConditionRepository>{

	public WeatherConditionDao(WeatherConditionRepository repository, String entityName) {
		super(repository, entityName);
	}
}
