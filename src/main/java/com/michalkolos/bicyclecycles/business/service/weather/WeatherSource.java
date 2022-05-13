package com.michalkolos.bicyclecycles.business.service.weather;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;

import java.util.Collection;
import java.util.Set;

public interface WeatherSource {

	Set<Weather> get(Collection<City> cities);
}
