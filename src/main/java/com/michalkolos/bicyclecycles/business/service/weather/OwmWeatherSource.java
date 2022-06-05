package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.OwmWeather;

import java.util.Collection;
import java.util.Set;

public interface OwmWeatherSource {

	Set<OwmWeather> get(Collection<City> cities);
}
