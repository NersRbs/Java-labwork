package ru.gunkin.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;

@Component
@RequiredArgsConstructor
public class CatConsumer {
    private final CatServices catServices;

    @KafkaListener(topics = "addCat", groupId = "cat-group")
    public void listenAddCat(CatDto catDto) {
        catServices.addCat(catDto);
    }

    @KafkaListener(topics = "updateCat", groupId = "cat-group")
    public void listenUpdateCat(CatDto catDto) {
        catServices.update(catDto);
    }

    @KafkaListener(topics = "addFriend", groupId = "cat-group")
    public void listenAddFriend(FriendDto friendDto) {
        catServices.addFriend(friendDto);
    }

    @KafkaListener(topics = "deleteFriend", groupId = "cat-group")
    public void listenDeleteFriend(FriendDto friendDto) {
        catServices.deleteFriend(friendDto);
    }

    @KafkaListener(topics = "deleteCat", groupId = "cat-group")
    public void listenDeleteCat(long id) {
        catServices.deleteCat(id);
    }
}
