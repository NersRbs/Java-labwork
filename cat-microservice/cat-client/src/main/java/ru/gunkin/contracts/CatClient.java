package ru.gunkin.contracts;

import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;

public interface CatClient {
    Optional<CatDto> findCat(long id);

    List<CatDto> getCatFriends(long id);

    List<CatDto> findCats(String name, Breed breed, Color color, Long ownerId);
}
