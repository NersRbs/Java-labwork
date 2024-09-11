

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import ru.gunkin.abstractions.OwnerRepository;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;
import ru.gunkin.mappers.OwnerMapper;
import ru.gunkin.mappers.OwnerMapperImpl;
import ru.gunkin.models.Owner;
import ru.gunkin.services.OwnerServicesImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OwnerServiceTests {

    private static OwnerServices ownerServices;
    private static HashMap<Long, Owner> owners;

    @BeforeAll
    public static void setUp() {
        OwnerRepository ownersRepository = mock(OwnerRepository.class);
        OwnerMapper ownerMapper = new OwnerMapperImpl();
        @SuppressWarnings("unchecked")
        KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);


        ownerServices = new OwnerServicesImpl(ownersRepository, ownerMapper, kafkaTemplate);
        owners = new HashMap<>();

        doAnswer(invocation -> {
            Owner owner = invocation.getArgument(0);
            owners.put(owner.getId(), owner);
            return null;
        }).when(ownersRepository).save(any(Owner.class));

        doAnswer(invocation -> {
            long id = invocation.getArgument(0);
            owners.remove(id);
            return null;
        }).when(ownersRepository).deleteById(anyLong());

        doAnswer(invocation -> {
            long id = invocation.getArgument(0);
            return Optional.ofNullable(owners.get(id));
        }).when(ownersRepository).findById(anyLong());

        doAnswer(invocation -> new ArrayList<>(owners.values())).when(ownersRepository).findAll();

    }

    @Test
    public void addTest() {
        // Arrange
        OwnerDto ownerDto = OwnerDto.builder()
                .name("John")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        // Act
        ownerServices.addOwner(
                AddOwnerDto.builder()
                        .username("John")
                        .ownerDto(ownerDto)
                        .build()
        );

        // Assert
        assertEquals(1, owners.size());
        owners.clear();
    }

    @Test
    public void deleteTest() {
        // Arrange
        var owner = Owner.builder()
                .id(0L)
                .name("John")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        // Act
        owners.put(0L, owner);
        ownerServices.deleteOwner(0L);

        // Assert
        assertEquals(0, owners.size());
    }

    @Test
    public void getOwnerTest() {
        // Arrange
        var owner = Owner.builder()
                .id(0L)
                .name("John")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        // Act
        owners.put(0L, owner);
        var ownerDto = ownerServices.findOwner(0L);

        // Assert
        assertTrue(ownerDto.isPresent());
        assertEquals(ownerDto.get().getName(), "John");
        assertEquals(ownerDto.get().getBirthDate(), LocalDate.of(1990, 5, 15));
        owners.clear();
    }

    @Test
    public void getOwnersTest() {
        // Arrange
        var owner1 = Owner.builder()
                .id(0L)
                .name("John")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        var owner2 = Owner.builder()
                .id(1L)
                .name("Jane")
                .birthDate(LocalDate.of(1995, 3, 25))
                .build();

        // Act
        owners.put(0L, owner1);
        owners.put(1L, owner2);
        var ownerDtoArray = ownerServices.findAllOwners();

        // Assert
        assertEquals(ownerDtoArray.size(), 2);
        owners.clear();
    }
}