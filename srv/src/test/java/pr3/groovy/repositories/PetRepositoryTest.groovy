package pr3.groovy.repositories


import com.sap.cds.impl.ResultImpl
import com.sap.cds.services.ServiceException
import com.sap.cds.services.persistence.PersistenceService
import pr3.repositories.PetRepository
import pr3.utils.TestUtils
import spock.lang.Specification

class PetRepositoryTest extends Specification {

    def db = Mock(PersistenceService)
    def petRepository = new PetRepository(db)

    def "getPet should return pet"() {

        def pet = TestUtils.createPet(TestUtils.CAT)
        def result = ResultImpl.insertedRows(Collections.singletonList(pet)).result()
        db.run(_) >> result

        when:
        def receivedPet = petRepository.getPet(pet.getId())
        def receivedId = receivedPet
                .orElseThrow({ -> new ServiceException("Pet not found or doesn't exist") })
                .getId();

        then:
        receivedId == pet.getId()
    }

    def "getPet should return optional empty"() {

        def pet = TestUtils.createPet(TestUtils.CAT)
        pet.setId(TestUtils.invalidId())
        def result = ResultImpl.create().result()
        db.run(_) >> result

        when:
        def actualPet = petRepository.getPet(pet.getId())

        then:
        !actualPet.isPresent()
    }

    def "getPetsByTypeForUser should return pets"() {

        def userId = TestUtils.validId()
        def pets = TestUtils.createPets()
        def result = ResultImpl.insertedRows(pets).result()
        db.run(_) >> result

        when:
        def actualPets = petRepository.getPetsByTypeForUser(TestUtils.CAT, userId)

        then:
        !actualPets.isEmpty()
    }

    def "getPetsByTypeForUser with invalid userId should return empty list"() {

        def userId = TestUtils.invalidId()
        def result = ResultImpl.create().result()
        db.run(_) >> result

        when:
        def actualPets = petRepository.getPetsByTypeForUser(TestUtils.CAT, userId)

        then:
        actualPets.isEmpty()
    }

    def "getPetsByTypeForUser with unknown pet type should return empty list"() {

        def userId = TestUtils.validId()
        def result = ResultImpl.create().result()
        db.run(_) >> result

        when:
        def actualPets = petRepository.getPetsByTypeForUser("unknownType", userId)

        then:
        actualPets.isEmpty()
    }

    def "updatePet should return pet"() {
        def pet = TestUtils.createPet(TestUtils.CAT)
        def result = ResultImpl.insertedRows(Collections.singletonList(pet)).result()
        db.run(_) >> result

        when:
        def actualPet = petRepository.updatePet(pet)

        then:
        actualPet.getId() == pet.getId()
    }
}
