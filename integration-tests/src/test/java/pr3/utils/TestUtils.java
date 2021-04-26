package pr3.utils;

import cds.gen.catalogservice.Users;
import pr3.dto.UserDto;

import static java.lang.Math.random;
import static pr3.dto.UserDto.builder;

public class TestUtils {

    public static final String CAT = "CAT";
    public static final String URL_ODATA_V4_CS = "/odata/v4/CatalogService/";
    public static final String URL_CS_CHANGE_USER = "/CatalogService.changeUser";
    public static final String URL_CS_ATTACH_USER = "/CatalogService.attachUser";

    public static final Integer VALID_ID = 2;

    public static Integer generateInvalidId() {
        return (int) (-random());
    }

    public static UserDto wrapUserIdByDto(Integer userId) {
        Users user = Users.create();
        user.setId(userId);
        return builder().user(user).build();
    }
}
