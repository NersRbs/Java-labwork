package ru.gunkin.mappers;

import org.mapstruct.Mapper;
import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Cat;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CatMapper {
    CatDto toDto(Cat model);

    List<CatDto> toDto(List<Cat> model);

    Cat toModel(CatDto dto);

    List<Cat> toModel(List<CatDto> dto);
}
