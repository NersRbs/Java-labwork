package ru.gunkin.contracts;


import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;

public interface CatController {
    void addCat(CatDto catDto);

    void updateCat(long id, CatDto catDto);

    void deleteCat(long id);

    void addCatFriend(long cat_id, long friend_id);

    void deleteCatFriend(long cat_id, long friend_id);

    CatDto getCat(long id);

    List<CatDto> getCatFriends(long id);

    List<CatDto> getCats(String name, Breed breed, Color color);
}
