package ru.gunkin.contracts;


import ru.gunkin.dto.OwnerDto;

import java.util.List;
import java.util.Optional;

public interface OwnerController {
    void addOwner(String username, OwnerDto ownerDto);

    void updateOwner(OwnerDto ownerDto);

    void deleteOwner(long id);

    Optional<OwnerDto> findOwner(long id);

    List<OwnerDto> findOwners();
}
