package pr3.services;

import cds.gen.catalogservice.Pets;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repositories.PetRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pr3.utils.TestUtils.CAT;
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
        //Given
        Pets pet = createPet(CAT);
        when(petRepository.getPet(pet.getId()))
                .thenReturn(Optional.of(pet));
        //When
        Pets receivedPet = petService.getPet(pet.getId());
        //Then
        assertEquals(receivedPet.getId(), pet.getId());
    }

    @Test
    public void getStrangersTypedPets_GivenPetTypeAndUserId_ShouldReturnPets() {
        //Given
        List<Pets> pets = createPets();
        Integer userId = validId();
        when(petRepository.getStrangersTypedPets(CAT, userId)).thenReturn(pets);
        //When
        List<Pets> receivedPets = petService.getStrangersTypedPets(CAT, userId);
        //Then
        assertFalse(receivedPets.isEmpty());
    }

    @Test
    public void updatePet_GivenPet_ShouldReturnPet() {
        //Given
        Pets pet = createPet(CAT);
        when(petRepository.updatePet(pet))
                .thenReturn(pet);
        //When
        Pets updatedPet = petService.updatePet(pet);
        //Then
        assertEquals(updatedPet.getId(), pet.getId());
    }
}
