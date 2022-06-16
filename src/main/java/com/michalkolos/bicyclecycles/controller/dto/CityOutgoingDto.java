package com.michalkolos.bicyclecycles.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

@Data
@Builder
public class CityOutgoingDto {
	private long id;
	private String name;
	private String alias;
	private String country;
	private String bounds;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<PlaceOutgoingDto> places;
}
