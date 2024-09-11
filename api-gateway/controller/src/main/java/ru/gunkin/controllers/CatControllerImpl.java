package ru.gunkin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.gunkin.contracts.CatController;
import ru.gunkin.contracts.CatServices;
import ru.gunkin.contracts.UserService;
import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cats")
@RequiredArgsConstructor
public class CatControllerImpl implements CatController {
    private final CatServices catServices;
    private final UserService userService;

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public void addCat(@RequestBody CatDto catDto) {
        String username = userService.getUsername();
        catServices.addCat(username, catDto);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void updateCat(@PathVariable("id") long id , @RequestBody CatDto catDto) {
        String username = userService.getUsername();
        catDto.setId(id);
        catServices.update(username, catDto);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteCat(@PathVariable("id") long id) {
        String username = userService.getUsername();
        catServices.deleteCat(username, id);
    }

    @Override
    @PostMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasAuthority('USER')")
    public void addCatFriend(@PathVariable("catId") long catId, @PathVariable("friendId") long friendId) {
        String username = userService.getUsername();
        catServices.addFriend(username, catId, friendId);
    }

    @Override
    @DeleteMapping("/{catId}/friends/{friendId}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteCatFriend(@PathVariable("catId") long catId, @PathVariable("friendId") long friendId) {
        String username = userService.getUsername();
        catServices.deleteFriend(username, catId, friendId);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public CatDto getCat(@PathVariable("id") long id) {
        String username = userService.getUsername();
        return catServices.getCat(username, id);
    }

    @Override
    @GetMapping("/{id}/friends")
    @PreAuthorize("hasAuthority('USER')")
    public List<CatDto> getCatFriends(@PathVariable("id") long id) {
        String username = userService.getUsername();
        return catServices.getFriends(username, id);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<CatDto> getCats(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "breed", required = false) Breed breed,
            @RequestParam(value = "color", required = false) Color color) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return catServices.getAllCats(username, name, breed, color);
    }
}
