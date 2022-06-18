package com.michalkolos.bicyclecycles.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaceBikesOutgoingDto {
	private final long placeId;
	private final List<Long> bikeIds;
}
