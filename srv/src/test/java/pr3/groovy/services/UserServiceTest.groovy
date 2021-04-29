package pr3.groovy.services

import cds.gen.catalogservice.Users
import com.sap.cds.services.ServiceException
import pr3.repositories.UserRepository
import pr3.services.UserService
import spock.lang.Specification

import static pr3.utils.TestUtils.invalidId
import static pr3.utils.TestUtils.validId

class UserServiceTest extends Specification {

    def userRepository = Mock(UserRepository)
    def userService = new UserService(userRepository)

    def "getUser should return user"() {

        def userId = validId()
        def user = Users.create()
        user.setId(userId)

        userRepository.getUser(userId) >> user

        when:
        def actualUser = userService.getUser(userId)

        then:
        actualUser.getId() == user.getId()
    }

    def "getUser throws Service exception when gets invalid userId"() {

        def userId = invalidId()
        def user = Users.create()
        user.setId(userId)

        userRepository.getUser(userId) >> {
            throw new ServiceException("User not found or doesn't exist")
        }

        when:
        userService.getUser(userId)

        then:
        thrown(ServiceException)
    }
}