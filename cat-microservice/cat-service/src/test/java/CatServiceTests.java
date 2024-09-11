import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.gunkin.abstractions.CatRepository;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;
import ru.gunkin.mappers.CatMapper;
import ru.gunkin.mappers.CatMapperImpl;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Cat;
import ru.gunkin.models.Color;
import ru.gunkin.services.CatServicesImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CatServiceTests {

    private static CatServices catServices;
    private static HashMap<Long, Cat> cats;

    @BeforeAll
    public static void setUp() {
        CatRepository catsRepository = Mockito.mock(CatRepository.class);
        CatMapper catMapper = new CatMapperImpl();

        catServices = new CatServicesImpl(catsRepository, catMapper);
        cats = new HashMap<>();

        doAnswer(invocation -> {
            Cat cat = invocation.getArgument(0);
            cats.put(cat.getId(), cat);
            return null;
        }).when(catsRepository).save(any(Cat.class));

        doAnswer(invocation -> {
            Cat cat = invocation.getArgument(0);
            cats.remove(cat.getId());
            return null;
        }).when(catsRepository).delete(any(Cat.class));

        doAnswer(invocation -> {
            long id = invocation.getArgument(0);
            return Optional.ofNullable(cats.get(id));
        }).when(catsRepository).findById(anyLong());
    }

    @Test
    public void addTest() {
        // Arrange
        CatDto catDto = CatDto.builder()
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .build();

        // Act
        catServices.addCat(catDto);

        // Assert
        assertEquals(1, cats.size());
        cats.clear();
    }

    @Test
    public void deleteTest() {
        // Arrange
        var cat = Cat.builder()
                .id(0L)
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        // Act
        cats.put(0L, cat);

        catServices.deleteCat(0L);

        // Assert
        assertEquals(0, cats.size());
    }

    @Test
    public void addFriendTest() {
        // Arrange
        Cat cat = Cat.builder()
                .id(0L)
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        Cat friend = Cat.builder()
                .id(1L)
                .name("Murzik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        // Act
        cats.put(0L, cat);
        cats.put(1L, friend);
        catServices.addFriend(
                FriendDto.builder()
                        .catId(0L)
                        .friendId(1L)
                        .build()
        );

        // Assert
        assertEquals(1, cat.getFriends().size());
        assertEquals(1, friend.getFriends().size());
        cats.clear();
    }

    @Test
    public void deleteFriendTest() {
        // Arrange
        Cat cat = Cat.builder()
                .id(0L)
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        Cat friend = Cat.builder()
                .id(1L)
                .name("Murzik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        // Act
        cat.getFriends().add(friend);
        friend.getFriends().add(cat);

        cats.put(0L, cat);
        cats.put(1L, friend);

        catServices.deleteFriend(
                FriendDto.builder()
                        .catId(0L)
                        .friendId(1L)
                        .build()
        );

        // Assert
        assertEquals(0, cat.getFriends().size());
        assertEquals(0, friend.getFriends().size());
        cats.clear();
    }

    @Test
    public void getCatTest() {
        // Arrange
        Cat cat = Cat.builder()
                .id(0L)
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        // Act
        cats.put(0L, cat);

        var catDto = catServices.findCat(0L);

        // Assert
        assertTrue(catDto.isPresent());
        assertEquals("Barsik", catDto.get().getName());
        cats.clear();
    }

    @Test
    public void getCatFriends() {
        // Arrange
        Cat cat = Cat.builder()
                .name("Barsik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        Cat friend = Cat.builder()
                .name("Murzik")
                .birthDate(LocalDate.now())
                .breed(Breed.ABYSSINIAN)
                .color(Color.BLACK)
                .friends(new ArrayList<>())
                .build();

        // Act
        cat.getFriends().add(friend);
        friend.getFriends().add(cat);

        cats.put(0L, cat);
        cats.put(1L, friend);

        var catDtoArray = catServices.getFriends(0L);

        // Assert
        assertEquals(1, catDtoArray.size());
        cats.clear();
    }
}