package pr3.handlers;

import cds.gen.catalogservice.AttachUserContext;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.ChangeUserContext;
import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import cds.gen.catalogservice.Users;
import cds.gen.catalogservice.Users_;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pr3.provider.IdProvider;
import pr3.services.PetService;
import pr3.services.UserService;
import pr3.validators.CatalogServiceValidator;

import java.util.List;

@Component
@ServiceName(CatalogService_.CDS_NAME)
@RequiredArgsConstructor
public class CatalogServiceHandler implements EventHandler {

    private final PetService petService;
    private final UserService userService;
    private final IdProvider idProvider;
    private final CatalogServiceValidator catalogServiceValidator;

    @On(entity = Pets_.CDS_NAME)
    public void onChangeUser(ChangeUserContext context) {
        Integer petId = idProvider.getId(context.getCqn());
        Integer userId = context.getUser().getId();
        Pets pet = attachUserToPet(petId, userId);
        context.setResult(pet);
    }

    @On(entity = Users_.CDS_NAME)
    public void onAttachUser(AttachUserContext context) {
        String type = context.getType();
        Integer userId = idProvider.getId(context.getCqn());
        Users user = attachUserToPets(type, userId);
        context.setResult(user);
    }

    private Users attachUserToPets(String type, Integer userId) {
        Users user = userService.getUser(userId);
        List<Pets> resultPets = petService.getPetsByTypeForUser(type, userId);
        catalogServiceValidator.checkPetsExistence(resultPets, type);
        resultPets.forEach(pet -> attach(pet, userId));
        return user;
    }

    private Pets attachUserToPet(Integer petId, Integer userId) {
        Pets pet = petService.getPet(petId);
        return attach(pet, userId);
    }

    private Pets attach(Pets pet, Integer userId) {
        catalogServiceValidator.checkUserExistence(userId);
        catalogServiceValidator.checkAttaching(pet.getUserId(), userId);
        pet.setUserId(userId);
        pet = petService.updatePet(pet);
        return pet;
    }
}