package com.michalkolos.bicyclecycles.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BikeOutgoingDto {
	private long id;
	private long number;
	private int bikeType;
	private String lockTypes;
	private boolean isElectric;
}
