package pr3.validators;

import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.services.UserService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CatalogServiceValidatorTest {

    @Mock
    private UserService userService;

    private CatalogServiceValidator catalogServiceValidator;

    @Before
    public void setUp(){
        initMocks(this);
        catalogServiceValidator = new CatalogServiceValidator(userService);
    }

    @Test(expected = ServiceException.class)
    public void checkAttaching_SameGivenPetUserIdAndUserId_ShouldThrowServiceException(){
        //Given
        Integer petUserId = 1;
        Integer userId = petUserId;
        //When
        catalogServiceValidator.checkAttaching(petUserId, userId);
    }

    @Test(expected = ServiceException.class)
    public void checkUserExistence_GivenUserId_ShouldThrowServiceException(){
        //Given
        Integer userId = 1;
        when(userService.getUser(userId)).thenThrow(ServiceException.class);
        //When
        catalogServiceValidator.checkUserExistence(userId);
    }
}
