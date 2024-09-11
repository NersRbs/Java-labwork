package ru.gunkin.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.gunkin.abstractions.UserRepository;
import ru.gunkin.contracts.CatClient;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;
import ru.gunkin.resterror.exaptions.NotFoundException;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CatServicesImpl implements CatServices {
    private final UserRepository userRepository;
    private final CatClient catClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    @Transactional
    public void addCat(String username, CatDto catDto) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        catDto.setOwnerId(user.getOwnerId());

        kafkaTemplate.send("addCat", catDto);
    }

    @Override
    @Transactional
    public void update(String username, CatDto catDto) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));


        var cat = catClient.findCat(catDto.getId())
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (!user.getOwnerId().equals(cat.getOwnerId())) {
            throw new NotFoundException("Cat not found");
        }

        kafkaTemplate.send("updateCat", catDto);
    }

    @Override
    @Transactional
    public void addFriend(String username, long catId, long friendId) {
        checkCatAndFriend(username, catId, friendId);

        kafkaTemplate.send("addFriend", FriendDto.builder()
                .catId(catId)
                .friendId(friendId)
                .build());
    }

    @Override
    @Transactional
    public void deleteFriend(String username, long catId, long friendId) {
        checkCatAndFriend(username, catId, friendId);

        kafkaTemplate.send("deleteFriend", FriendDto.builder()
                .catId(catId)
                .friendId(friendId)
                .build());
    }

    @Override
    @Transactional
    public void deleteCat(String username, long id) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var cat = catClient.findCat(id)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (!user.getOwnerId().equals(cat.getOwnerId())) {
            throw new NotFoundException("Cat not found");
        }

        kafkaTemplate.send("deleteCat", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CatDto getCat(String username, long id) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var cat = catClient.findCat(id)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (!user.getOwnerId().equals(cat.getOwnerId())) {
            throw new NotFoundException("Cat not found");
        }

        return cat;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> getFriends(String username, long id) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var cat = catClient.findCat(id)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        if (!user.getOwnerId().equals(cat.getOwnerId())) {
            throw new NotFoundException("Cat not found");
        }

        return catClient.getCatFriends(cat.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatDto> getAllCats(String username, String name, Breed breed, Color color) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return catClient.findCats(name, breed, color, user.getOwnerId());
    }

    private void checkCatAndFriend(String username, long catId, long friendId) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        var cat = catClient.findCat(catId)
                .orElseThrow(() -> new NotFoundException("Cat not found"));

        var friend = catClient.findCat(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found"));

        if (!user.getOwnerId().equals(cat.getOwnerId())) {
            throw new NotFoundException("Cat not found");
        }

        if (!user.getOwnerId().equals(friend.getOwnerId())) {
            throw new NotFoundException("Friend not found");
        }
    }
}
