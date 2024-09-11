package ru.gunkin.contracts;

import ru.gunkin.dto.OwnerDto;

import java.util.List;
import java.util.Optional;


public interface OwnerClient {
    Optional<OwnerDto> findOwner(long id);

    List<OwnerDto> findOwners();
}
