/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.entity;

import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CityDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CountryDto;
import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "city_uid", nullable = false, unique = true)
	private long uid;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "organization_name")
	private String orgName;

	@Column(name = "zoom")
	private int zoom;

	@Column(name = "name")
	private String name;

	@Column(name = "alias")
	private String alias;

	@Column(name = "bounds",columnDefinition="Geometry")
	private Geometry bounds;

	@Column(name = "openweathermaps_id")
	private Long owmId;


	@OneToMany(fetch=FetchType.LAZY, mappedBy = "city", cascade={CascadeType.ALL})
	private Set<Place> places = new HashSet<>();



	public City() {
	}

	public City(long uid, String countryName, String orgName, int zoom,
	            String name, String alias, Geometry bounds) {

		this.uid = uid;
		this.countryName = countryName;
		this.orgName = orgName;
		this.zoom = zoom;
		this.name = name;
		this.alias = alias;
		this.bounds = bounds;
	}

	public City(CityDto cityDto, CountryDto countryDto) {

		this.uid = cityDto.getUid();
		this.countryName = countryDto.getCountry_name();
		this.orgName = countryDto.getName();
		this.zoom = cityDto.getZoom();
		this.name = cityDto.getName();
		this.alias = cityDto.getAlias();
		this.bounds = cityDto.getBounds();
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		City city = (City) o;
		return uid == city.uid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}

	@Override
	public String toString() {
		return "City{" +
				"id=" + id +
				", uid=" + uid +
				", countryName='" + countryName + '\'' +
				", orgName='" + orgName + '\'' +
				", zoom=" + zoom +
				", name='" + name + '\'' +
				", alias='" + alias + '\'' +
				", bounds=" + bounds +
				", owmId=" + owmId +
				'}';
	}
}
