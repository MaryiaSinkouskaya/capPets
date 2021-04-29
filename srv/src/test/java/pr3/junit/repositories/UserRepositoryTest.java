package pr3.junit.repositories;

import cds.gen.catalogservice.Users;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.persistence.PersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repositories.UserRepository;

import java.util.Optional;

import static com.sap.cds.impl.ResultImpl.create;
import static com.sap.cds.impl.ResultImpl.insertedRows;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static pr3.utils.TestUtils.createUser;

public class UserRepositoryTest {

    @Mock
    private PersistenceService db;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        openMocks(this);
        userRepository = new UserRepository(db);
    }

    @Test
    public void getUser_GivenUserId_ShouldReturnCertainUser() {
        //Given
        Users user = createUser();
        Result result = insertedRows(singletonList(user)).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);
        //When
        Optional<Users> receivedUser = userRepository.getUser(user.getId());
        Integer receivedId = receivedUser
                .orElseThrow(() -> new ServiceException("User not found or doesn't exist"))
                .getId();
        //Then
        assertEquals(receivedId, user.getId());
    }

    @Test
    public void getUser_GivenUserId_ShouldReturnOptionalEmpty() {
        //Given
        Users user = createUser();
        Result result = create().result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);
        //When
        Optional<Users> receivedUser = userRepository.getUser(user.getId());
        //Then
        assertFalse(receivedUser.isPresent());
    }
}