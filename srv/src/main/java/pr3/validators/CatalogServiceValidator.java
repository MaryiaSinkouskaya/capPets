package pr3.validators;

import cds.gen.catalogservice.Users;
import com.sap.cds.Result;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Component;
import pr3.service.PersistenceService;

@Component
public final class CatalogServiceValidator {

    private final PersistenceService persistenceService;

    public CatalogServiceValidator(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Users checkUserExistence(Integer userId) {
        Result resultUser = persistenceService.getUser(userId);
        checkResultInstance(resultUser);
        return resultUser.first().get().as(Users.class);
    }

    public void checkAttaching(Integer petId, Integer userId) {
        if (petId.equals(userId)) {
            throw new ServiceException("Pet already attached to this user");
        }
    }

    public void checkResultInstance(Result result) {
        if (!result.first().isPresent()) {
            throw new ServiceException("Instance not found or doesn't exist");
        }
    }

    public void checkResultInstances(Result result) {
        if (result.list().isEmpty()) {
            throw new ServiceException("Instances not found");
        }
    }
}
