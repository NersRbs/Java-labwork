package ru.gunkin.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;

@Component
@RequiredArgsConstructor
public class OwnerConsumer {
    private final OwnerServices ownerServices;

    @KafkaListener(topics = "addOwner", groupId = "owner-group")
    public void listenAddOwner(AddOwnerDto addDto) {
        ownerServices.addOwner(addDto);
    }

    @KafkaListener(topics = "updateOwner", groupId = "owner-group")
    public void listenUpdateOwner(OwnerDto ownerDto) {
        ownerServices.updateOwner(ownerDto);
    }

    @KafkaListener(topics = "deleteOwner", groupId = "owner-group")
    public void listenDeleteOwner(long id) {
        ownerServices.deleteOwner(id);
    }
}
