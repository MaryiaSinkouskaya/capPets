package pr3.service;

import cds.gen.catalogservice.Pets;
import com.sap.cds.Result;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Service;
import pr3.repository.PetRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PetService {

    private final PetRepository petRepository;


    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pets getPet(Integer petId) {
        Result resultPet = petRepository.getPet(petId);
        return resultPet.first(Pets.class)
                .orElseThrow(() -> new ServiceException("Pet not found or doesn't exist"));
    }

    public List<Pets> getStrangersTypedPets(String type, Integer userId) {
        Result resultPets = petRepository.getStrangersTypedPets(type, userId);
        return resultPets.stream()
                .map(pet -> pet.as(Pets.class))
                .collect(toList());
    }

    public Pets updatePet(Pets pet) {
        return petRepository.updatePet(pet);
    }
}
