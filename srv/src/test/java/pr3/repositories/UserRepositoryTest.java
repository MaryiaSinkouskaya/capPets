package pr3.repositories;

import cds.gen.catalogservice.Users;
import com.sap.cds.EmptyResultException;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.persistence.PersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static com.sap.cds.impl.ResultImpl.create;
import static com.sap.cds.impl.ResultImpl.insertedRows;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.createUser;

public class UserRepositoryTest {

    @Mock
    private PersistenceService db;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        initMocks(this);
        userRepository = new UserRepository(db);
    }

    @Test
    public void getUser_GivenUserId_ShouldReturnCertainUser() {
        Users user = createUser();
        Result result = insertedRows(singletonList(user)).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        Optional<Users> receivedUser = userRepository.getUser(user.getId());
        Integer receivedId = receivedUser
                .orElseThrow(() -> new ServiceException("User not found or doesn't exist"))
                .getId();
        assertEquals(receivedId, user.getId());
    }

    @Test(expected = EmptyResultException.class)
    public void getUser_GivenUserId_ShouldReturnOptionalEmpty() {
        Users user = createUser();
        Result result = create().result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        userRepository.getUser(user.getId());
    }

}
