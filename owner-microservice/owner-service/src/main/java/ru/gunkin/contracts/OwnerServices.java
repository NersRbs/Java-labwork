package ru.gunkin.contracts;


import ru.gunkin.dto.AddOwnerDto;
import ru.gunkin.dto.OwnerDto;

import java.util.List;
import java.util.Optional;

public interface OwnerServices {
    void addOwner(AddOwnerDto addDto);

    void updateOwner(OwnerDto ownerDto);

    void deleteOwner(long id);

    Optional<OwnerDto> findOwner(long id);

    List<OwnerDto> findAllOwners();
}
