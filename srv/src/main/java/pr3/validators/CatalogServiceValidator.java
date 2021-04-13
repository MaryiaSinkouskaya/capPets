package pr3.validators;

import com.sap.cds.Result;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Component;

@Component
public final class CatalogServiceValidator {

    public void checkAttaching(Integer petId, Integer userId) {
        if (petId.equals(userId)) {
            throw new ServiceException("Pet already attached to this user");
        }
    }

    public void checkResultInstances(Result result) {
        if (result.list().isEmpty()) {
            throw new ServiceException("Instances not found");
        }
    }
}
