package pr3.validators;

import cds.gen.catalogservice.Pets;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pr3.repositories.UserRepository;
import pr3.services.UserService;

import java.util.List;

import static com.sap.cds.services.ErrorStatuses.BAD_REQUEST;
import static com.sap.cds.services.ErrorStatuses.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CatalogServiceValidator {

    private final UserService userService;

    public void checkAttaching(Integer petUserId, Integer userId) {
        if (petUserId.equals(userId)) {
            throw new ServiceException(BAD_REQUEST, "Pet already attached to this user");
        }
    }

    public void checkPetsExistence(List<Pets> pets, String type) {
        if(pets.isEmpty()){
            throw new ServiceException(NOT_FOUND, type + " Pets not found, already attached or such type doesn't exist");
        }
    }

    public void checkUserExistence(Integer userId){
        userService.getUser(userId);
    }
}
