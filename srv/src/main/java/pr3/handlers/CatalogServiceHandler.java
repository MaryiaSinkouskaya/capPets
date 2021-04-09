package pr3.handlers;

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
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.stereotype.Component;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.ServiceName;


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

		Integer userId = context.getUser().getId();
		Integer petId = (Integer) analyzer.analyze(context.getCqn()).targetKeys().get("ID");

		Result resultPet = db.run(Select.from(pets).where(pets_ -> pets_.ID().eq(petId)));
		Result resultUser = db.run(Select.from(users).where(user -> user.ID().eq(userId)));

		if (!resultPet.first().isPresent()) {
			throw new ServiceException("Pet not found");
		}
		if (!resultUser.first().isPresent()) {
			throw new ServiceException("User not found");
		}

		Pets pet = resultPet.first().get().as(Pets.class);
		Users user = resultUser.first().get().as(Users.class);

		if (pet.getUserId().equals(user.getId())) {
			throw new ServiceException("Pet already attached to this user");
		}

		pet.setUserId(user.getId());
		pet = db.run(Update.entity(pets).data(pet)).single(Pets.class);
		context.setResult(pet);
	}
}