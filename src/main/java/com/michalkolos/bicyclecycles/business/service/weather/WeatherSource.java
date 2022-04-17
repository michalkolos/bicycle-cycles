package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface WeatherSource {
	Optional<Weather> getPointWeather(City city);
	Set<Weather> getGroupWeather(Collection<City> cities);
}
