package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps.dto;

import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Weather;
import com.michalkolos.bicyclecycles.entity.WeatherCondition;

public class OwmCityDtoToWeatherConverter {
	public static Weather convert(OwmCityDto dto) {
		Weather weather = new Weather();

		weather.setClouds(dto.getClouds());
		weather.setHumidity(dto.getHumidity());
		weather.setHumanTemp(dto.getHuman_temp());
		weather.setTemp(dto.getTemp());
		weather.setPressure(dto.getPressure());
		weather.setRain(dto.getRain());
		weather.setSnow(dto.getSnow());
		weather.setPressure(dto.getPressure());
		weather.setWindDeg(dto.getWind_deg());
		weather.setWindSpeed(dto.getWind_speed());
		weather.setCalculatedTime(dto.getCalculatedTime());
		weather.setSunrise(dto.getSunrise());
		weather.setSunset(dto.getSunset());

		weather.setCondition(new WeatherCondition(dto.getCondition_id(), dto.getDescription()));

		return weather;
	}

	public static Weather convert(OwmCityDto dto, City city) {
		Weather weather =convert (dto);
		weather.setCity(city);

		return weather;
	}
}
