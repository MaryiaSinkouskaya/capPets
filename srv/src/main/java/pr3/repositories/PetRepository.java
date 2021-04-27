package pr3.repositories;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import com.sap.cds.EmptyResultException;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class PetRepository {

    private final PersistenceService db;

    public Optional<Pets> getPet(Integer petId) {
        Select<Pets_> select = Select
                .from(Pets_.class)
                .where(pets_ -> pets_.ID().eq(petId));
        try {
            Pets receivedPet = db.run(select).single(Pets.class);
            return ofNullable(receivedPet);
        } catch (EmptyResultException e) {
            return empty();
        }
    }

    public List<Pets> getPetsByTypeForUser(String type, Integer userId) {
        Select<Pets_> select = Select.from(Pets_.class)
                .where(pet -> pet
                        .type().eq(type)
                        .and(pet.user_ID().ne(userId)));
        try {
            return db.run(select).listOf(Pets.class);
        } catch (EmptyResultException e) {
            return emptyList();
        }
    }

    public Pets updatePet(Pets pet) {
        return db.run(Update.entity(Pets_.class).data(pet)).single(Pets.class);
    }
}
