package pr3.services;

import cds.gen.catalogservice.Users;
import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repository.UserRepository;
import pr3.service.UserService;

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
        Users user = createUser();
        when(userRepository.getUser(user.getId()))
                .thenReturn(Optional.of(user));
        Users user1 = userService.getUser(user.getId());

        assertEquals(user1.getId(), user.getId());
    }

    @Test
    public void getUser_GivenUserId_ShouldThrowException() {
        Integer invalidId = invalidId();
        Users user = createUser();
        user.setId(invalidId);
        when(userRepository.getUser(invalidId))
                .thenThrow(new ServiceException("User not found or doesn't exist"));
        try {
            userService.getUser(invalidId);
        } catch (ServiceException e) {
            assertEquals(e.getMessage(), "User not found or doesn't exist");
        }
    }
}