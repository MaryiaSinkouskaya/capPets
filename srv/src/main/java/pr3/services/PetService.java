package pr3.services;

import cds.gen.catalogservice.Pets;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pr3.repositories.PetRepository;

import java.util.List;
import java.util.Optional;

import static com.sap.cds.services.ErrorStatuses.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public Pets getPet(Integer petId) {
        Optional<Pets> pet = petRepository.getPet(petId);
        return pet.orElseThrow(() -> new ServiceException(NOT_FOUND, "Pet not found or doesn't exist"));
    }

    public List<Pets> getPetsByTypeForUser(String type, Integer userId) {
        return petRepository.getPetsByTypeForUser(type, userId);
    }

    public Pets updatePet(Pets pet) {
        return petRepository.updatePet(pet);
    }
}
