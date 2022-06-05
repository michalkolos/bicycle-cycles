package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.OwmWeather;
import com.michalkolos.bicyclecycles.entity.OwmWeatherCondition;

public class OwmCityDtoToWeatherConverter {
	public static OwmWeather convert(OwmCityDto dto) {
		OwmWeather owmWeather = new OwmWeather();

		//  TODO: Introduce builder

		owmWeather.setClouds(dto.getClouds());
		owmWeather.setHumidity(dto.getHumidity());
		owmWeather.setHumanTemp(dto.getHuman_temp());
		owmWeather.setTemp(dto.getTemp());
		owmWeather.setPressure(dto.getPressure());
		owmWeather.setRain(dto.getRain());
		owmWeather.setSnow(dto.getSnow());
		owmWeather.setPressure(dto.getPressure());
		owmWeather.setWindDeg(dto.getWind_deg());
		owmWeather.setWindSpeed(dto.getWind_speed());
		owmWeather.setCalculatedTime(dto.getCalculatedTime());
		owmWeather.setSunrise(dto.getSunrise());
		owmWeather.setSunset(dto.getSunset());
		owmWeather.setVisibility(dto.getVisibility());

		owmWeather.setCondition(new OwmWeatherCondition(
				dto.getCondition_id(),
				dto.getCondition_name(),
				dto.getDescription()));

		return owmWeather;
	}

	public static OwmWeather convert(OwmCityDto dto, City city) {
		OwmWeather owmWeather =convert (dto);
		owmWeather.setCity(city);

		return owmWeather;
	}
}
