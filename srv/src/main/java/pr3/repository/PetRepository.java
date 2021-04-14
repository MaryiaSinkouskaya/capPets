package pr3.repository;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.stereotype.Repository;

@Repository
public class PetRepository {

    private final PersistenceService db;

    public PetRepository(PersistenceService db) {
        this.db = db;
    }

    public Result getPet(Integer petId) {
        return db.run(Select.from(Pets_.class).where(pets_ -> pets_.ID().eq(petId)));
    }

    public Result getStrangersTypedPets(String type, Integer userId) {
        return db.run(Select.from(Pets_.class).where(pet -> pet
                .type().eq(type)
                .and(pet.user_ID().ne(userId))));
    }

    public Pets updatePet(Pets pet) {
        return db.run(Update.entity(Pets_.class).data(pet)).single(Pets.class);
    }
}
