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
import org.springframework.stereotype.Component;
import pr3.service.PersistenceService;
import pr3.utils.IdProvider;
import pr3.validators.CatalogServiceValidator;

import java.util.List;


@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

    private final PersistenceService persistenceService;
    private final IdProvider idProvider;
    private final CatalogServiceValidator catalogServiceValidator;

    public CatalogServiceHandler(PersistenceService persistenceService, IdProvider idProvider, CatalogServiceValidator catalogServiceValidator) {
        this.persistenceService = persistenceService;
        this.idProvider = idProvider;
        this.catalogServiceValidator = catalogServiceValidator;
    }

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
        Users user = persistenceService.getUser(userId);
        List<Pets> resultPets = persistenceService.getTypedPets(type);
        resultPets.stream()
                .filter(pet -> !pet.getUserId().equals(userId))
                .forEach(pet -> attach(pet, userId));
        return user;
    }

    private Pets attachUserToPet(Integer petId, Integer userId) {
        Pets pet = persistenceService.getPet(petId);
        return attach(pet, userId);
    }

    private Pets attach(Pets pet, Integer userId) {
        catalogServiceValidator.checkAttaching(pet.getUserId(), userId);
        pet.setUserId(userId);
        pet = persistenceService.updatePet(pet);
        return pet;
    }

}