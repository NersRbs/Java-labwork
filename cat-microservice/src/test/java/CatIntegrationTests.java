import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.gunkin.CatApplication;
import ru.gunkin.abstractions.CatRepository;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Cat;
import ru.gunkin.models.Color;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CatApplication.class)
@AutoConfigureMockMvc
public class CatIntegrationTests {
    private final MockMvc mockMvc;
    private final CatRepository catRepository;
    private Long catId;

    @Autowired
    public CatIntegrationTests(MockMvc mockMvc,  CatRepository catRepository) {
        this.mockMvc = mockMvc;
        this.catRepository = catRepository;
    }


    @BeforeEach
    public void setup() {
        var cat = Cat.builder()
                .name("Barsik")
                .birthDate(LocalDate.of(2010, 1, 1))
                .breed(Breed.BENGAL)
                .color(Color.BLACK)
                .ownerId(1L)
                .build();
        catRepository.save(cat);
        catId = cat.getId();
    }

    @AfterEach
    public void tearDown() {
        catRepository.deleteById(catId);
    }

    @Test
    public void testUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/cats/" + catId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Murka\", " +
                                "\"birthDate\":\"2000-01-01\", " +
                                "\"breed\":\"BURMILLA\", " +
                                "\"color\":\"WHITE\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats/" + catId))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.name").value("Murka"))
                .andExpect(jsonPath("$.breed").value("BURMILLA"))
                .andExpect(jsonPath("$.color").value("WHITE"));
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats/" + catId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.breed").exists())
                .andExpect(jsonPath("$.color").exists());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testGetByColor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/cats?color=BLACK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
