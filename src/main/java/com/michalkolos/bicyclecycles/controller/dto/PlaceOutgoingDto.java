package com.michalkolos.bicyclecycles.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@Builder
public class PlaceOutgoingDto {
	private long id;
	private String position;
	private String name;
	private int rackNo;
}
