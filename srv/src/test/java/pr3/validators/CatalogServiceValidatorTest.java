package pr3.validators;

import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;

public class CatalogServiceValidatorTest {

    private CatalogServiceValidator catalogServiceValidator;

    @Before
    public void setUp(){
        catalogServiceValidator = new CatalogServiceValidator();
    }

    @Test(expected = ServiceException.class)
    public void checkAttaching_SameGivenPetUserIdAndUserId_ShouldThrowServiceException(){
        Integer petUserId = 1;
        Integer userId = petUserId;
        catalogServiceValidator.checkAttaching(petUserId, userId);
    }
}
