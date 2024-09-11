package ru.gunkin.mappers;

import org.mapstruct.Mapper;
import ru.gunkin.dto.OwnerDto;
import ru.gunkin.models.Owner;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDto toDto(Owner model);

    List<OwnerDto> toDto(List<Owner> model);

    Owner toModel(OwnerDto dto);

    List<Owner> toModel(List<OwnerDto> dto);
}