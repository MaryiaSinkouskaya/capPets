package pr3.validators;

import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Component;

import static com.sap.cds.services.ErrorStatuses.BAD_REQUEST;

@Component
public class CatalogServiceValidator {

    public void checkAttaching(Integer petUserId, Integer userId) {
        if (petUserId.equals(userId)) {
            throw new ServiceException(BAD_REQUEST, "Pet already attached to this user");
        }
    }
}
