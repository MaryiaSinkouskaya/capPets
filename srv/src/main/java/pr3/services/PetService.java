package pr3.services;

import cds.gen.catalogservice.Pets;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pr3.repository.PetRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public Pets getPet(Integer petId) {
        Optional<Pets> pet = petRepository.getPet(petId);
        return pet.orElseThrow(() -> new ServiceException("Pet not found or doesn't exist"));
    }

    public List<Pets> getStrangersTypedPets(String type, Integer userId) {
        return petRepository.getStrangersTypedPets(type, userId);
    }

    public Pets updatePet(Pets pet) {
        return petRepository.updatePet(pet);
    }
}
