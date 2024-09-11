import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.gunkin.OwnerApplication;
import ru.gunkin.abstractions.OwnerRepository;
import ru.gunkin.models.Owner;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = OwnerApplication.class)
@AutoConfigureMockMvc
public class OwnersIntegrationTests {
    private final MockMvc mockMvc;
    private final OwnerRepository ownerRepository;

    private Long ownerId;

    @Autowired
    public OwnersIntegrationTests(MockMvc mockMvc, OwnerRepository ownerRepository) {
        this.mockMvc = mockMvc;
        this.ownerRepository = ownerRepository;
    }


    @BeforeEach
    public void setup() {
        var owner = new Owner("John", LocalDate.now());
        ownerRepository.save(owner);
        ownerId = ownerRepository.findAll().get(0).getId();
    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteById(ownerId);
    }

    @Test
    public void testUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/owners")
                        .contentType("application/json")
                        .content("{\"id\":\"" + ownerId + "\", " +
                                "\"name\":\"Kracker\", " +
                                "\"birthDate\":\"2000-01-01\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/owners/" + ownerId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
