/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.entity;


import lombok.*;
import org.locationtech.jts.geom.Point;
import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Place {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uid", nullable = false)
	private long uid;

	@Column(name = "number", nullable = false)
	private long number;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "position")
	private Point position;

	@Column(name = "rack_no", nullable = false)
	private int rackNo;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;
}
