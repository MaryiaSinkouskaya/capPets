package pr3.repositories;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Pets_;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnUpdate;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.persistence.PersistenceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static com.sap.cds.ResultBuilder.insertedRows;
import static com.sap.cds.impl.ResultImpl.create;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.createPet;
import static pr3.utils.TestUtils.createPets;
import static pr3.utils.TestUtils.invalidId;
import static pr3.utils.TestUtils.validId;

public class PetRepositoryTest {

    @Mock
    private PersistenceService db;

    private PetRepository petRepository;

    @Before
    public void setUp() {
        initMocks(this);
        petRepository = new PetRepository(db);
    }

    @Test
    public void getPet_GivenPetId_ShouldReturnCertainPet() {
        Pets pet = createPet("CAT");
        Result result = insertedRows(singletonList(pet)).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        Optional<Pets> receivedPet = petRepository.getPet(pet.getId());
        Integer receivedId = receivedPet
                .orElseThrow(() -> new ServiceException("Pet not found or doesn't exist"))
                .getId();
        assertEquals(receivedId, pet.getId());
    }

    @Test
    public void getPet_GivenPetId_ShouldReturnOptionalEmpty() {
        Pets pet = createPet("CAT");
        Result result = create().result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        Optional<Pets> receivedPet = petRepository.getPet(pet.getId());
        assertFalse(receivedPet.isPresent());
    }

    @Test
    public void getStrangersTypedPets_GivenPetTypeAndUserId_ShouldReturnPets() {
        List<Pets> pets = createPets();
        Integer userId = validId();
        Result result = insertedRows(pets).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        List<Pets> resultPets = petRepository.getStrangersTypedPets("CAT", userId);

        assertFalse(resultPets.isEmpty());
    }

    @Test
    public void getStrangersTypedPets_GivenUnknownPetTypeAndUserId_ShouldReturnEmptyList() {
        Integer userId = validId();
        Result result = insertedRows(emptyList()).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        List<Pets> resultPets = petRepository.getStrangersTypedPets("UNKNOWN_TYPE", userId);

        assertTrue(resultPets.isEmpty());
    }

    @Test
    public void getStrangersTypedPets_GivenPetTypeAndInvalidUserId_ShouldReturnEmptyList() {
        Integer userId = invalidId();
        Result result = insertedRows(emptyList()).result();

        Select anySelect = any(Select.class);
        when(db.run(anySelect)).thenReturn(result);

        List<Pets> resultPets = petRepository.getStrangersTypedPets("CAT", userId);

        assertTrue(resultPets.isEmpty());
    }

    @Test
    public void updatePet_GivenPet_ShouldReturnPet() {
        Pets pet = createPet("CAT");
        Result result = insertedRows(singletonList(pet)).result();

        when(db.run(any(CqnUpdate.class))).thenReturn(result);

        Pets receivedPet = petRepository.updatePet(pet);

        assertEquals(receivedPet, pet);
    }

}
