package pr3.groovy.validators

import com.sap.cds.services.ServiceException
import pr3.services.UserService
import pr3.validators.CatalogServiceValidator
import spock.lang.Specification

import static pr3.utils.TestUtils.validId

class CatalogServiceValidatorTest extends Specification {

    def userService = Mock(UserService)
    def catalogServiceValidator = new CatalogServiceValidator(userService)


    //todo: rename all def's
    def "checkAttaching should throw ServiceException"() {

        def id = validId()

        when:
        catalogServiceValidator.checkAttaching(id, id)

        then:
        thrown(ServiceException)
    }

    def "checkUserExistence should throw ServiceException"() {

        Integer userId = validId()
        userService.getUser(userId) >> {
            throw new ServiceException("User not found or doesn't exist")
        }

        when:
        catalogServiceValidator.checkUserExistence(userId)

        then:
        thrown(ServiceException)
    }
}
