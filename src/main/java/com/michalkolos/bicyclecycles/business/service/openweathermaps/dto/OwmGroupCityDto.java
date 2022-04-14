/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.openweathermaps.dto;

import lombok.Data;

import java.util.Set;


@Data
public class OwmGroupCityDto {
	private int cnt;
	private Set<OwmCityDto> list;
}
