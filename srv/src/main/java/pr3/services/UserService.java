package pr3.services;

import cds.gen.catalogservice.Users;
import com.sap.cds.services.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pr3.repositories.UserRepository;

import java.util.Optional;

import static com.sap.cds.services.ErrorStatuses.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Users getUser(Integer userId) {
        Optional<Users> user = userRepository.getUser(userId);
        return user.orElseThrow(() -> new ServiceException(NOT_FOUND, "User not found or doesn't exist"));
    }
}
