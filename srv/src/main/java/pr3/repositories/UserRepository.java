package pr3.repositories;

import cds.gen.catalogservice.Users;
import cds.gen.catalogservice.Users_;
import com.sap.cds.EmptyResultException;
import com.sap.cds.ql.Select;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final PersistenceService db;

    public Optional<Users> getUser(Integer userId) {
        Select<Users_> select = Select
                .from(Users_.class)
                .where(user_ -> user_.ID().eq(userId));
        try {
            Users receivedUser = db.run(select).single(Users.class);
            return ofNullable(receivedUser);
        } catch (EmptyResultException e) {
            return empty();
        }
    }
}
