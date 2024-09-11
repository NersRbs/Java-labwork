package ru.gunkin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gunkin.contracts.CatController;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.dto.CatDto;
import ru.gunkin.dto.FriendDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cats")
@RequiredArgsConstructor
public class CatControllerImpl implements CatController {
    private final CatServices catServices;

    @Override
    @PostMapping
    public void addCat(@RequestBody CatDto catDto) {
        catServices.addCat(catDto);
    }

    @Override
    @PutMapping("/{id}")
    public void updateCat(@PathVariable("id") long id, @RequestBody CatDto catDto) {
        catDto.setId(id);
        catServices.update(catDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteCat(@PathVariable("id") long id) {
        catServices.deleteCat(id);
    }

    @Override
    @PostMapping("/{catId}/friends/{friendId}")
    public void addCatFriend(@PathVariable("catId") long catId, @PathVariable("friendId") long friendId) {
        catServices.addFriend(
                FriendDto.builder()
                        .catId(catId)
                        .friendId(friendId)
                        .build()
        );
    }

    @Override
    @DeleteMapping("/{catId}/friends/{friendId}")
    public void removeCatFriend(@PathVariable("catId") long catId, @PathVariable("friendId") long friendId) {
        catServices.deleteFriend(
                FriendDto.builder()
                        .catId(catId)
                        .friendId(friendId)
                        .build()
        );
    }

    @Override
    @GetMapping("/{id}")
    public Optional<CatDto> findCat(@PathVariable("id") long id) {
        return catServices.findCat(id);
    }

    @Override
    @GetMapping("/{id}/friends")
    public List<CatDto> getCatFriends(@PathVariable("id") long id) {
        return catServices.getFriends(id);
    }

    @Override
    @GetMapping
    public List<CatDto> findCats(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "breed", required = false) Breed breed,
            @RequestParam(value = "color", required = false) Color color,
            @RequestParam(value = "ownerId", required = false) Long ownerId) {
        return catServices.findAllCats(name, breed, color, ownerId);
    }
}
