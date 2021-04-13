package pr3.repository;

import cds.gen.catalogservice.Users_;
import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final PersistenceService db;

    public UserRepository(PersistenceService db) {
        this.db = db;
    }

    public Result getUser(Integer userId){
        return db.run(Select.from(Users_.class).where(user_ -> user_.ID().eq(userId)));
    }
}
