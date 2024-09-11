package ru.gunkin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.gunkin.contracts.OwnerController;
import ru.gunkin.contracts.OwnerServices;
import ru.gunkin.contracts.UserService;
import ru.gunkin.dto.OwnerDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owners")
@RequiredArgsConstructor
public class OwnerControllerImpl implements OwnerController {
    private final OwnerServices ownerServices;
    private final UserService userService;

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public void addOwner(@RequestBody OwnerDto ownerDto) {
        String username = userService.getUsername();
        ownerServices.addOwner(username, ownerDto);
    }

    @Override
    @PutMapping
    @PreAuthorize("hasAuthority('USER')")
    public void updateOwner(@RequestBody OwnerDto ownerDto) {
        String username = userService.getUsername();
        ownerServices.updateOwner(username, ownerDto);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteOwner(@PathVariable("id") long id) {
        ownerServices.deleteOwner(id);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OwnerDto getOwner(@PathVariable("id") long id) {
        return ownerServices.getOwner(id);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OwnerDto> getOwners() {
        return ownerServices.getAllOwners();
    }
}
