package pr3.groovy.services

import cds.gen.catalogservice.Pets
import com.sap.cds.services.ServiceException
import pr3.repositories.PetRepository
import pr3.services.PetService
import spock.lang.Specification

import static pr3.utils.TestUtils.validId
import static pr3.utils.TestUtils.invalidId
import static pr3.utils.TestUtils.createPets
import static pr3.utils.TestUtils.CAT

class PetServiceTest extends Specification {

    def petRepository = Mock(PetRepository)
    def petService = new PetService(petRepository)

    def "getPet should return pet"() {

        def petId = validId()
        def pet = Pets.create()
        pet.setId(petId)

        petRepository.getPet(petId) >> pet

        when:
        def actualPet = petService.getPet(petId)

        then:
        actualPet.getId() == pet.getId()
    }

    def "getPet throws Service exception when gets invalid userId"() {

        def petId = invalidId()
        def pet = Pets.create()
        pet.setId(petId)

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
}
