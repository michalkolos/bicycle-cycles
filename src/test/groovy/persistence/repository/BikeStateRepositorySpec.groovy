package persistence.repository

import com.michalkolos.bicyclecycles.entity.BikeState
import com.michalkolos.bicyclecycles.persistence.repository.BikeStateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class BikeStateRepositorySpec extends Specification{

    @Autowired
    BikeStateRepository bikeStateRepository

    def "Can fetch all BikeStates from the database"() {
        when:
        List<BikeState> bikeStates = bikeStateRepository.findAll()
        then:
        bikeStates.size() != 0
    }
}
