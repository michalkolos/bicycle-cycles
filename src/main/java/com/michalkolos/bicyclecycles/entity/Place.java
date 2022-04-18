/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;


import lombok.*;
import org.locationtech.jts.geom.Point;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
public class Place {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "place_uid", nullable = false, unique = true)
	private long uid;

	@Column(name = "number", nullable = false)
	private long number;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "position", nullable = false)
	private Point position;

	@Column(name = "rack_no", nullable = false)
	private int rackNo;

	@OneToMany(fetch=FetchType.LAZY, mappedBy = "place", cascade={CascadeType.ALL})
	private Set<BikeState> bikeStates = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "city_id", nullable = false)
	private City city;


	public Place() {
	}

	public Place(long uid, long number, String name, Point position, int rackNo,
	             City city) {

		this.uid = uid;
		this.number = number;
		this.name = name;
		this.position = position;
		this.rackNo = rackNo;
		this.city = city;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Place place = (Place) o;
		return uid == place.uid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}
}
