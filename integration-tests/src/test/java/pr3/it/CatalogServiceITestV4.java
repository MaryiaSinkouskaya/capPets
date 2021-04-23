package pr3.it;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pr3.Application;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CatalogServiceITestV4 {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testReadPets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/odata/v4/CatalogService/Pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value[0].ID").value(1))
                .andExpect(jsonPath("$.value[0].name").value("catto"))
                .andExpect(jsonPath("$.value[1].ID").value(2))
                .andExpect(jsonPath("$.value[1].name").value("doggo"));
    }

    @Test
    public void testChangeUser() throws Exception {
        int petId = 2;
        int userId = 2;
        mockMvc.perform(MockMvcRequestBuilders
                .post("/odata/v4/CatalogService/Pets(" + petId + ")/CatalogService.changeUser")
                .contentType(APPLICATION_JSON)
                .content("{\n" +
                        "    \"user\": {\n" +
                        "        \"ID\": " + userId + "\n" +
                        "    }\n" +
                        "}")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_ID").value(userId));
    }
}
