package ru.gunkin.contracts;


import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;

public interface CatServices {
    void addCat(String username, CatDto catDto);

    void update(String username, CatDto catDto);

    void addFriend(String username, long catId, long friendId);

    void deleteFriend(String username, long catId, long friendId);

    void deleteCat(String username, long id);

    CatDto getCat(String username, long id);

    List<CatDto> getFriends(String username, long id);

    List<CatDto> getAllCats(String username, String name, Breed breed, Color color);
}
