package ru.gunkin.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gunkin.abstractions.UserRepository;
import ru.gunkin.contracts.CatClient;
import ru.gunkin.contracts.OwnerClient;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;
import ru.gunkin.resterror.exaptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerServicesImpl implements OwnerServices {
    private final UserRepository userRepository;
    private final OwnerClient ownerClient;
    private final CatClient catClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void addOwner(String username, OwnerDto ownerDto) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getOwnerId() != null) {
            throw new NotFoundException("User already exists");
        }

        kafkaTemplate.send("addOwner", AddOwnerDto.builder()
                .username(username)
                .ownerDto(ownerDto)
                .build());
    }


    @Override
    @Transactional
    public void updateOwner(String username, OwnerDto ownerDto) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getOwnerId().equals(ownerDto.getId())) {
            throw new NotFoundException("Owner not found");
        }

        kafkaTemplate.send("updateOwner", ownerDto);
    }

    @Override
    @Transactional
    public void deleteOwner(long id) {
        var user = userRepository.findByOwnerId(id)
                .orElseThrow(() -> new NotFoundException("Owner not found"));

        userRepository.delete(user);

        var catDto = catClient.findCats(null, null, null, id);
        for (var cat : catDto) {
            kafkaTemplate.send("deleteCat", cat.getId());
        }

        kafkaTemplate.send("deleteOwner", id);
    }


    @Override
    public OwnerDto getOwner(long id) {
        return ownerClient.findOwner(id)
                .orElseThrow(() -> new NotFoundException("Owner not found"));
    }

    @Override
    public List<OwnerDto> getAllOwners() {
        return ownerClient.findOwners();
    }
}
