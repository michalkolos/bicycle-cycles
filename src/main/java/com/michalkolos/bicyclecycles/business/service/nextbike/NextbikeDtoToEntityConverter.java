package com.michalkolos.bicyclecycles.business.service.nextbike;

import com.michalkolos.bicyclecycles.business.service.nextbike.dto.BikeDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CityDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.CountryDto;
import com.michalkolos.bicyclecycles.business.service.nextbike.dto.PlaceDto;
import com.michalkolos.bicyclecycles.entity.Bike;
import com.michalkolos.bicyclecycles.entity.BikeState;
import com.michalkolos.bicyclecycles.entity.City;
import com.michalkolos.bicyclecycles.entity.Place;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NextbikeDtoToEntityConverter {

	GeometryFactory geometryFactory;

	@Autowired
	public NextbikeDtoToEntityConverter(GeometryFactory geometryFactory) {
		this.geometryFactory = geometryFactory;
	}

	public BikeState buildBikeStateFromScratch(CountryDto countryDto,
	                                           CityDto cityDto,
	                                           PlaceDto placeDto,
	                                           BikeDto bikeDto) {

		City city = buildCity(countryDto, cityDto);
		Place place = buildPlace(placeDto, city);
		Bike bike = buildBike(bikeDto);

		return buildBikeState(bikeDto, bike, place);
	}

	public City buildCity(CountryDto countryDto, CityDto cityDto) {
		City city = new City();

		city.setUid(cityDto.getUid());
		city.setCountryName(countryDto.getCountry_name());
		city.setOrgName(countryDto.getName());
		city.setZoom(cityDto.getZoom());
		city.setName(cityDto.getName());
		city.setAlias(cityDto.getAlias());
		city.setBounds(cityDto.getBounds());

		return city;
	}

	public Place buildPlace(PlaceDto placeDto, City city){
		Place place = new Place();

		Point position = geometryFactory.createPoint(
				new Coordinate(placeDto.getLng(), placeDto.getLat()));

		place.setUid(placeDto.getUid());
		place.setNumber(placeDto.getNumber());
		place.setName(placeDto.getName());
		place.setPosition(position);
		place.setRackNo(place.getRackNo());

		place.setCity(city);

		return place;
	}

	public Bike buildBike(BikeDto bikeDto) {
		Bike bike = new Bike();

		bike.setNumber(bikeDto.getNumber());
		bike.setBikeType(bikeDto.getBike_type());
		bike.setLockTypes(bikeDto.getLock_types());
		bike.setElectric(bikeDto.isElectric());

		return bike;
	}

	public BikeState buildBikeState(BikeDto bikeDto, Bike bike, Place place) {
		BikeState bikeState = new BikeState();

		bikeState.setState(bikeDto.getState());
		bikeState.setActive(bikeDto.getActive() != 0);
		bikeState.setBatteryLevel(bikeDto.getPedelec_battery());
		bikeState.setElectricLock(bikeState.isElectricLock());

		bikeState.setBike(bike);
		bikeState.setPlace(place);

		return bikeState;
	}
}
