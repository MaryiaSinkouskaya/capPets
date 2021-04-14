package pr3.service;

import cds.gen.catalogservice.Users;
import com.sap.cds.Result;
import com.sap.cds.services.ServiceException;
import org.springframework.stereotype.Service;
import pr3.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getUser(Integer userId) {
        Result resultUser = userRepository.getUser(userId);
        return resultUser.first(Users.class)
                .orElseThrow(() -> new ServiceException("User not found or doesn't exist"));

    }
}
