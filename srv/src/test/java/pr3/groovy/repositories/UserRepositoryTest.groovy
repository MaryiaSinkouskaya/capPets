package pr3.groovy.repositories


import com.sap.cds.impl.ResultImpl
import com.sap.cds.services.ServiceException
import com.sap.cds.services.persistence.PersistenceService
import pr3.repositories.UserRepository
import pr3.utils.TestUtils
import spock.lang.Specification

import static com.sap.cds.impl.ResultImpl.create

class UserRepositoryTest extends Specification {

    def db = Mock(PersistenceService)
    def userRepository = new UserRepository(db)

    def "getUser should return user"() {
        def user = TestUtils.createUser()
        def result = ResultImpl.insertedRows(Collections.singletonList(user)).result()
        db.run(_) >> result

        when:
        def actualUser = userRepository.getUser(user.getId())

        def actualId = actualUser
                .orElseThrow({ -> new ServiceException("User not found or doesn't exist") })
                .getId()

        then:
        user.getId() == actualId
    }

    def "getUser should return optional empty"() {
        def result = create().result()
        db.run(_) >> result

        when:
        def actualUser = userRepository.getUser(TestUtils.validId())

        then:
        !actualUser.isPresent()
    }


}