package pr3.handlers;

import cds.gen.catalogservice.AttachUserContext;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.ChangeUserContext;
import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import cds.gen.catalogservice.Users;
import cds.gen.catalogservice.Users_;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.stereotype.Component;


@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

    private final PersistenceService db;
    private final CqnAnalyzer analyzer;

    private final Class<Users_> users = Users_.class;
    private final Class<Pets_> pets = Pets_.class;

    public CatalogServiceHandler(PersistenceService db, CdsModel model) {
        this.db = db;
        this.analyzer = CqnAnalyzer.create(model);
    }

    @On(entity = Pets_.CDS_NAME)
    public void onChangeUser(ChangeUserContext context) {
        Integer petId = (Integer) analyzer.analyze(context.getCqn()).targetKeys().get("ID");
        Integer userId = context.getUser().getId();
        Pets pet = attachUserToPet(petId, userId);
        context.setResult(pet);
    }

    @On(entity = Users_.CDS_NAME)
    public void onAttachUser(AttachUserContext context) {
        String type = context.getType();
        Integer userId = (Integer) analyzer.analyze(context.getCqn()).targetKeys().get("ID");
        Users user = checkUserExistence(userId);
        attachUserToPets(type, userId);
        context.setResult(user);
    }

    private Pets attach(Pets pet, Integer userId) {
        if (pet.getUserId().equals(userId)) {
            throw new ServiceException("Pet already attached to this user");
        }
        pet.setUserId(userId);
        pet = db.run(Update.entity(pets).data(pet)).single(Pets.class);
        return pet;
    }

    private Users checkUserExistence(Integer userId) {
        Result resultUser = db.run(Select.from(users)
                .where(user_ -> user_.ID().eq(userId)));
        if (!resultUser.first().isPresent()) {
            throw new ServiceException("User not found or doesn't exist");
        }
        return resultUser.first().get().as(Users.class);
    }

    private void attachUserToPets(String type, Integer userId){
        Result resultPets = db.run(Select.from(pets)
                        .where(pet -> pet.type().eq(type)));

        if (resultPets.list().isEmpty()) {
            throw new ServiceException("Pets not found");
        }

        resultPets.stream()
                .map(pet -> pet.as(Pets.class))
                .filter(pet->!pet.getUserId().equals(userId))
                .forEach(pet -> attach(pet, userId));
    }

    private Pets attachUserToPet(Integer petId, Integer userId) {
        checkUserExistence(userId);
        Result resultPet = db.run(Select.from(pets).where(pets_ -> pets_.ID().eq(petId)));

        if (!resultPet.first().isPresent()) {
            throw new ServiceException("Pet not found or doesn't exist");
        }

        Pets pet = resultPet.first().get().as(Pets.class);
        return attach(pet, userId);
    }
}