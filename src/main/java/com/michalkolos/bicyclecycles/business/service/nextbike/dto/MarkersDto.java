/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.nextbike.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;


@Data
public class MarkersDto {
	Set<CountryDto> country = new HashSet<>();
}
