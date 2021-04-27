package pr3.services;

import cds.gen.catalogservice.Pets;
import com.sap.cds.services.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pr3.repositories.PetRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static pr3.utils.TestUtils.CAT;
import static pr3.utils.TestUtils.createPet;
import static pr3.utils.TestUtils.createPets;
import static pr3.utils.TestUtils.invalidId;
import static pr3.utils.TestUtils.validId;

public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    private PetService petService;


    @Before
    public void setUp() {
        openMocks(this);
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

    @Test(expected = ServiceException.class)
    public void getPet_GivenInvalidPetId_ShouldThrowServiceException() {
        //Given
        Pets pet = createPet(CAT);
        when(petRepository.getPet(pet.getId()))
                .thenThrow(new ServiceException("Pet not found or doesn't exist"));
        //When
        Pets receivedPet = petService.getPet(pet.getId());
    }

    @Test
    public void getStrangersTypedPets_GivenPetTypeAndUserId_ShouldReturnPets() {
        //Given
        List<Pets> pets = createPets();
        Integer userId = validId();
        when(petRepository.getPetsByTypeForUser(CAT, userId)).thenReturn(pets);
        //When
        List<Pets> receivedPets = petService.getStrangersTypedPets(CAT, userId);
        //Then
        assertFalse(receivedPets.isEmpty());
    }

    @Test
    public void getStrangersTypedPets_GivenPetTypeAndInvalidUserId_ShouldReturnEmptyList() {
        //Given
        Integer userId = invalidId();
        when(petRepository.getPetsByTypeForUser(CAT, userId)).thenReturn(emptyList());
        //When
        List<Pets> receivedPets = petService.getStrangersTypedPets(CAT, userId);
        //Then
        assertTrue(receivedPets.isEmpty());
    }

    @Test
    public void getStrangersTypedPets_GivenPetUnknownTypeAndUserId_ShouldReturnEmptyList() {
        //Given
        String type = "unknownType";
        Integer userId = invalidId();
        when(petRepository.getPetsByTypeForUser(type, userId)).thenReturn(emptyList());
        //When
        List<Pets> receivedPets = petService.getStrangersTypedPets(type, userId);
        //Then
        assertTrue(receivedPets.isEmpty());
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
