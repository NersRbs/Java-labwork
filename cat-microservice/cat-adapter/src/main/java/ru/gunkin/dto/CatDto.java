package ru.gunkin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CatDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Breed breed;
    private Color color;
    private Long ownerId;
}
