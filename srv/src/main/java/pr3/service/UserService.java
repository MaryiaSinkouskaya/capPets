package pr3.service;

import cds.gen.catalogservice.Users;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pr3.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Users getUser(Integer userId) {
        Optional<Users> user = userRepository.getUser(userId);
        return user.orElseThrow(() -> new ServiceException("User not found or doesn't exist"));
    }
}
