package com.michalkolos.bicyclecycles.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class SampleOutgoingDto {
	private final long id;
	private final Instant timestamp;
	private final List<PlaceBikesOutgoingDto> places;
}
