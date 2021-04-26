package pr3.it;

import cds.gen.catalogservice.Pets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pr3.Application;
import pr3.dto.UserDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pr3.utils.TestUtils.CAT;
import static pr3.utils.TestUtils.VALID_ID;
import static pr3.utils.TestUtils.generateInvalidId;
import static pr3.utils.TestUtils.wrapUserIdByDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CatalogServiceITestV4 {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testReadPets_ShouldReturnPets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/odata/v4/CatalogService/Pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value[0].ID").value(1))
                .andExpect(jsonPath("$.value[0].name").value("catto"))
                .andExpect(jsonPath("$.value[1].ID").value(2))
                .andExpect(jsonPath("$.value[1].name").value("doggo"));
    }

    @Test
    public void onChangeUser_GivenPetIdAndUserId_ShouldReturnPetWithNewUserId() throws Exception {
        int petId = VALID_ID;
        int userId = VALID_ID;
        UserDto userDto = wrapUserIdByDto(userId);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Pets(" + petId + ")/CatalogService.changeUser")
                .contentType(APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_ID").value(userId));
    }

    @Test
    public void onChangeUser_GivenInvalidPetId_ShouldThrowServiceException() throws Exception {
        int petId = generateInvalidId();
        int userId = VALID_ID;
        UserDto userDto = wrapUserIdByDto(userId);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Pets(" + petId + ")/CatalogService.changeUser")
                .contentType(APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void onChangeUser_GivenInvalidUserId_ShouldThrowServiceException() throws Exception {
        int petId = VALID_ID;
        int userId = generateInvalidId();
        UserDto userDto = wrapUserIdByDto(userId);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Pets(" + petId + ")/CatalogService.changeUser")
                .contentType(APPLICATION_JSON)
                .content(userDto.toJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void onAttachUser_GivenUserIdAndType_ShouldReturnUser() throws Exception {
        int userId = VALID_ID;
        Pets pets = Pets.create();
        pets.setType(CAT);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Users(" + userId + ")/CatalogService.attachUser")
                .contentType(APPLICATION_JSON)
                .content(pets.toJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ID").value(userId));
    }

    @Test
    public void onAttachUser_GivenInvalidUserIdAndType_ShouldThrowServiceException() throws Exception {
        int userId = generateInvalidId();
        Pets pets = Pets.create();
        pets.setType(CAT);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Users(" + userId + ")/CatalogService.attachUser")
                .contentType(APPLICATION_JSON)
                .content(pets.toJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void onAttachUser_GivenUserIdAndInvalidType_ShouldThrowServiceException() throws Exception {
        int userId = VALID_ID;
        Pets pets = Pets.create();
        pets.setType("unknownType");
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Users(" + userId + ")/CatalogService.attachUser")
                .contentType(APPLICATION_JSON)
                .content(pets.toJson()))
                .andExpect(status().isNotFound());
    }
}
