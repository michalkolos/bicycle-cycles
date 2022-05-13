/*
 * Copyright (c) 2022  Michal Kolosowski <michalkoloso@gmail.com>
 */

package com.michalkolos.bicyclecycles.business.service.weather.openweathermaps

import com.michalkolos.bicyclecycles.entity.City
import com.michalkolos.bicyclecycles.entity.Weather
import com.michalkolos.bicyclecycles.utils.DownloaderService
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import com.michalkolos.bicyclecycles.business.configuration.DeserializerConfiguration
import spock.lang.Specification


class OwmWeatherSourceSpec extends Specification {

//  Class to be tested
    private OwmWeatherSource weatherSource

//  Dependencies
    private GeometryFactory geometryFactory

//  Test object
    private City sampleCity;

    void setup() {
        geometryFactory = new GeometryFactory()

        Envelope envelope = new Envelope(
                new Coordinate(-3.49, 50.72), new Coordinate(-3.5 , 50.73))
        Geometry bounds = geometryFactory.toGeometry(envelope)

        sampleCity = new City(1, "Poland", "Acme",
                2, "Szamotuly", "Szamotuly-alias",
                bounds)

        weatherSource = new OwmWeatherSource(
                new DeserializerConfiguration().objectMapper(), new DownloaderService())
    }


    void "For a given city with Owm ID weather data is downloaded and converted to Weather objects inside Cities"() {
        given: "a collection with one city that has a valid valid OpenWeather api ID "
        sampleCity.setOwmId(3083878)
        Set<City> cities = new HashSet<>();
        cities.add(sampleCity)

        when: "we ask to fetch recent weather for the given city"
        Set<Weather> weathers = weatherSource.get(cities)

        then: "we get a set containing one Weather object"
        weathers.size() == 1
    }

    void "For a given city without Owm ID weather data is downloaded and converted to Weather objects inside Cities"() {
        given: "a collection with one city that does not have a valid valid OpenWeather api ID "
        Set<City> cities = new HashSet<>();
        cities.add(sampleCity)

        when: "we ask to fetch recent weather for the given city"
        Set<Weather> weathers = weatherSource.get(cities)

        then: "we get a set containing one Weather object"
        weathers.size() == 1
    }

    void "For a given city without Owm ID and baounds no weather data is fetched, and empty set is returned"() {
        given: "a collection with one city that does not have a valid valid OpenWeather api ID and bounds"
        sampleCity.setBounds(null)
        Set<City> cities = new HashSet<>();
        cities.add(sampleCity)

        when: "we ask to fetch recent weather for the given city"
        Set<Weather> weathers = weatherSource.get(cities)

        then: "we get an empty set"
        weathers.size() == 0
    }


}