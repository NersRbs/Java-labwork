package ru.gunkin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gunkin.contracts.OwnerController;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerControllerImpl implements OwnerController {
    private final OwnerServices ownerServices;

    @Override
    @PostMapping("/{username}")
    public void addOwner(@PathVariable("username") String username, @RequestBody OwnerDto ownerDto) {
        var addDto = new AddOwnerDto(username, ownerDto);
        ownerServices.addOwner(addDto);
    }


    @Override
    @PutMapping
    public void updateOwner(@RequestBody OwnerDto ownerDto) {
        ownerServices.updateOwner(ownerDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public void deleteOwner(@PathVariable("id") long id) {
        ownerServices.deleteOwner(id);
    }

    @Override
    @GetMapping("/{id}")
    public Optional<OwnerDto> findOwner(@PathVariable("id") long id) {
        return ownerServices.findOwner(id);
    }

    @Override
    @GetMapping
    public List<OwnerDto> findOwners() {
        return ownerServices.findAllOwners();
    }
}
