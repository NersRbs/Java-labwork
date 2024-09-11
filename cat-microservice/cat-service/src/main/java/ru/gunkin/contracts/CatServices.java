package ru.gunkin.contracts;

import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;

public interface CatServices {
    void addCat(CatDto catDto);

    void update(CatDto catDto);

    void addFriend(FriendDto friendDto);

    void deleteFriend(FriendDto friendDto);

    void deleteCat(long id);

    Optional<CatDto> findCat(long id);

    List<CatDto> getFriends(long id);

    List<CatDto> findAllCats(String name, Breed breed, Color color, Long ownerId);
}
