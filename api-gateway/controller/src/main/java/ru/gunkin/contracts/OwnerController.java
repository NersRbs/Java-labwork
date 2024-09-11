package ru.gunkin.contracts;

import ru.gunkin.dto.OwnerDto;

import java.util.List;

public interface OwnerController {
    void addOwner(OwnerDto ownerDto);

    void updateOwner(OwnerDto ownerDto);

    void deleteOwner(long id);

    OwnerDto getOwner(long id);

    List<OwnerDto> getOwners();
}
