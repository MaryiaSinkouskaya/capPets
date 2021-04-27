package pr3.dto;

import cds.gen.catalogservice.Users;
import com.sap.cds.JSONizable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto implements JSONizable {

    private Users user;

    @Override
    public String toJson() {
        return "{ "
                + "\"user\" :"
                + user.toJson()
                + "}";
    }
}
