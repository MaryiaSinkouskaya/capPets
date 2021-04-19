package pr3.services;

import cds.gen.catalogservice.Pets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repository.PetRepository;
import pr3.service.PetService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.createPet;
import static pr3.utils.TestUtils.createPets;
import static pr3.utils.TestUtils.validId;

public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    private PetService petService;

    @Before
    public void setUp() {
        initMocks(this);
        petService = new PetService(petRepository);
    }

    @Test
    public void getPet_GivenPetId_ShouldReturnPet() {
        Pets pet = createPet("CAT");
        when(petRepository.getPet(pet.getId()))
                .thenReturn(Optional.of(pet));
        Pets pet1 = petService.getPet(pet.getId());

        assertEquals(pet1.getId(), pet.getId());
    }


    @Test
    public void getStrangersTypedPets_GivenPetTypeAndUserId_ShouldReturnPets() {
        List<Pets> pets = createPets();
        String type = "CAT";
        Integer userId = validId();
        when(petRepository.getStrangersTypedPets(type, userId))
                .thenReturn(pets);
        List<Pets> receivedPets = petService.getStrangersTypedPets(type, userId);
        assertFalse(receivedPets.isEmpty());
    }


    @Test
    public void updatePet_GivenPet_ShouldReturnPet() {
        Pets pet = createPet("CAT");
        when(petRepository.updatePet(pet))
                .thenReturn(pet);
        Pets updatedPet = petService.updatePet(pet);
        assertEquals(updatedPet.getId(), pet.getId());
    }
}
