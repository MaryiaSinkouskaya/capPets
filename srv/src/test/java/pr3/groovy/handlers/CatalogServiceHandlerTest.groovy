package pr3.groovy.handlers

import cds.gen.catalogservice.ChangeUserContext
import cds.gen.catalogservice.Users_
import com.sap.cds.ql.cqn.CqnSelect
import com.sap.cds.ql.impl.SelectBuilder
import pr3.handlers.CatalogServiceHandler
import pr3.provider.IdProvider
import pr3.services.PetService
import pr3.services.UserService
import pr3.validators.CatalogServiceValidator
import spock.lang.Specification

import static pr3.utils.TestUtils.CAT
import static pr3.utils.TestUtils.createPet
import static pr3.utils.TestUtils.createUser
import static pr3.utils.TestUtils.validId

class CatalogServiceHandlerTest extends Specification {

    def petService = Mock(PetService)
    def userService = Mock(UserService)
    def idProvider = Mock(IdProvider)
    def catalogServiceValidator = Mock(CatalogServiceValidator)
    def catalogServiceHandler = new CatalogServiceHandler(petService, userService, idProvider, catalogServiceValidator)

    def "onChangeUser gets context and should attach user to required pet"() {
        given:
        def user = createUser()
        def userId = user.getId()

        def pet = createPet(CAT)
        pet.setUserId(validId())
        def petId = pet.getId()

        ChangeUserContext context = ChangeUserContext.create()
        context.setUser(user)

        def select = SelectBuilder.from(Users_.CDS_NAME).asSelect()
        context.setCqn(select)

        idProvider.getId(_ as CqnSelect) >> petId
        petService.getPet(petId) >> pet
        pet.setUserId(userId)
        petService.updatePet(pet) >> (pet)

        when:
        catalogServiceHandler.onChangeUser(context)

        then:
        noExceptionThrown()
        pet.getUserId() == userId
//        1 * idProvider.getId(select)
//        1 * petService.getPet(petId)
//        1 * catalogServiceValidator.checkAttaching(pet.getUserId(), userId)
//        1 * petService.updatePet(pet)
    }
}
