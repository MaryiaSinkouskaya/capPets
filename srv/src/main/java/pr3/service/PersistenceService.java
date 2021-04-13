package pr3.service;

import cds.gen.catalogservice.Pets;
import cds.gen.catalogservice.Users;
import com.sap.cds.Result;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Service;
import pr3.repository.PetRepository;
import pr3.repository.UserRepository;
import pr3.validators.CatalogServiceValidator;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PersistenceService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final CatalogServiceValidator catalogServiceValidator;


    public PersistenceService(PetRepository petRepository, UserRepository userRepository, CatalogServiceValidator catalogServiceValidator) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.catalogServiceValidator = catalogServiceValidator;
    }

    public Pets getPet(Integer petId) {
        Result resultPet = petRepository.getPet(petId);
        return resultPet.first(Pets.class)
                .orElseThrow(() -> new ServiceException("Pet not found or doesn't exist"));
    }

    public List<Pets> getTypedPets(String type) {
        Result resultPets = petRepository.getTypedPets(type);
        catalogServiceValidator.checkResultInstances(resultPets);
        return resultPets.stream()
                .map(pet -> pet.as(Pets.class))
                .collect(toList());
    }

    public Pets updatePet(Pets pet) {
        return petRepository.updatePet(pet);
    }

    public Users getUser(Integer userId) {
        Result resultUser = userRepository.getUser(userId);
        return resultUser.first(Users.class)
                .orElseThrow(() -> new ServiceException("User not found or doesn't exist"));

    }
}
