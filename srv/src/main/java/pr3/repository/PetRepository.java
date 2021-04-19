package pr3.repository;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class PetRepository {

    private final PersistenceService db;

    public Optional<Pets> getPet(Integer petId) {
        Select<Pets_> select = Select
                .from(Pets_.class)
                .where(pets_ -> pets_.ID().eq(petId));
        return ofNullable(db.run(select).single(Pets.class));
    }

    public List<Pets> getStrangersTypedPets(String type, Integer userId) {
        Select<Pets_> select = Select.from(Pets_.class)
                .where(pet -> pet
                        .type().eq(type)
                        .and(pet.user_ID().ne(userId)));
        return db.run(select).listOf(Pets.class);
    }

    public Pets updatePet(Pets pet) {
        return db.run(Update.entity(Pets_.class).data(pet)).single(Pets.class);
    }
}
