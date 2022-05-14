package com.michalkolos.bicyclecycles.persistence.dao

import com.michalkolos.bicyclecycles.entity.Weather
import spock.lang.Specification

class WeatherDaoSpec extends Specification{

    private Set<Weather> incomingWeathers = new HashSet<>()

    setup() {
        incomingWeathers.add(new Weather())
    }

    void "Incoming Weather object is persisted"

}
