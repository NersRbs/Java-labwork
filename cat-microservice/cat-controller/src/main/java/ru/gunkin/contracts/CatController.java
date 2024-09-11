package ru.gunkin.contracts;

import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;


public interface CatController {
    void addCat(CatDto catDto);

    void updateCat(long id, CatDto catDto);

    void deleteCat(long id);

    void addCatFriend(long cat_id, long friend_id);

    void removeCatFriend(long cat_id, long friend_id);

    Optional<CatDto> findCat(long id);

    List<CatDto> getCatFriends(long id);

    List<CatDto> findCats(String name, Breed breed, Color color, Long ownerId);
}
