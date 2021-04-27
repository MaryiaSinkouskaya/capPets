package pr3.services;

import cds.gen.catalogservice.Users;
import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.createUser;
import static pr3.utils.TestUtils.invalidId;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void getUser_GivenUserId_ShouldReturnUser() {
        //Given
        Users user = createUser();
        when(userRepository.getUser(user.getId()))
                .thenReturn(Optional.of(user));
        //When
        Users receivedUser = userService.getUser(user.getId());
        //Then
        assertEquals(receivedUser.getId(), user.getId());
    }

    @Test(expected = ServiceException.class)
    public void getUser_GivenInvalidUserId_ShouldThrowException() {
        //Given
        Users user = createUser();
        Integer invalidId = invalidId();
        user.setId(invalidId);
        when(userRepository.getUser(invalidId))
                .thenThrow(new ServiceException("User not found or doesn't exist"));
        //When
        userService.getUser(invalidId);
    }
}
