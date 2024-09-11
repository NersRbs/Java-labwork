package ru.gunkin.contracts;


import ru.gunkin.dto.OwnerDto;

import java.util.List;

public interface OwnerServices {
    void addOwner(String username, OwnerDto ownerDto);

    void updateOwner(String username, OwnerDto ownerDto);

    void deleteOwner(long id);

    OwnerDto getOwner(long id);

    List<OwnerDto> getAllOwners();
}
