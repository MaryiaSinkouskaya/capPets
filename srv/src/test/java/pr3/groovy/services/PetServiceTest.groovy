package pr3.groovy.services


import com.sap.cds.services.ServiceException
import pr3.repositories.PetRepository
import pr3.services.PetService
import pr3.utils.TestUtils
import spock.lang.Specification

import static java.util.Collections.emptyList
import static pr3.utils.TestUtils.CAT
import static pr3.utils.TestUtils.createPets
import static pr3.utils.TestUtils.invalidId
import static pr3.utils.TestUtils.validId

class PetServiceTest extends Specification {

    def petRepository = Mock(PetRepository)
    def petService = new PetService(petRepository)

    def "getPet should return pet"() {

        def pet = TestUtils.createPet(CAT)
        def petId = pet.getId()

        petRepository.getPet(petId) >> pet

        when:
        def actualPet = petService.getPet(petId)

        then:
        actualPet.getId() == pet.getId()
    }

    def "getPet throws Service exception when gets invalid userId"() {

        def pet = TestUtils.createPet(CAT)
        def petId = pet.getId()

        petRepository.getPet(petId) >> {
            throw new ServiceException("Pet not found or doesn't exist")
        }

        when:
        petService.getPet(petId)

        then:
        thrown(ServiceException)
    }

    def "getPet throws Service exception when gets invalid petId"() {

        def pet = TestUtils.createPet(CAT)
        pet.setId(invalidId())
        def petId = pet.getId()

        petRepository.getPet(petId) >> {
            throw new ServiceException("Pet not found or doesn't exist")
        }

        when:
        petService.getPet(petId)

        then:
        thrown(ServiceException)
    }


    def "getPetsByTypeForUser should return pets"() {

        def pets = createPets()
        def userId = validId()

        petRepository.getPetsByTypeForUser(CAT, userId) >> pets

        when:
        def actualPets = petService.getPetsByTypeForUser(CAT, userId)

        then:
        actualPets == pets
    }

    def "getPetsByTypeForUser with invalid UserId should return empty list"() {

        def userId = invalidId()

        petRepository.getPetsByTypeForUser(CAT, userId) >> emptyList()

        when:
        def actualPets = petService.getPetsByTypeForUser(CAT, userId)

        then:
        actualPets.isEmpty()
    }

    def "getPetsByTypeForUser with unknown type should return empty list"() {

        def userId = validId()

        petRepository.getPetsByTypeForUser("unknownType", userId) >> emptyList()

        when:
        def actualPets = petService.getPetsByTypeForUser("unknownType", userId)

        then:
        actualPets.isEmpty()
    }

    def "updatePet should return pet"() {
        def pet = TestUtils.createPet(CAT)

        petRepository.updatePet(pet) >> pet

        when:
        def actualPet = petService.updatePet(pet)

        then:
        actualPet == pet
    }
}
