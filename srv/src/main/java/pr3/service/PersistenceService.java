package pr3.service;

import cds.gen.catalogservice.Pets;
import com.sap.cds.Result;
import org.springframework.stereotype.Service;
import pr3.repository.PetRepository;
import pr3.repository.UserRepository;

@Service
public class PersistenceService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public PersistenceService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    public Result getPet(Integer petId) {
        return petRepository.getPet(petId);
    }

    public Result getTypedPets(String type) {
        return petRepository.getTypedPets(type);
    }

    public Pets updatePet(Pets pet) {
        return petRepository.updatePet(pet);
    }

    public Result getUser(Integer userId) {
        return userRepository.getUser(userId);
    }
}
