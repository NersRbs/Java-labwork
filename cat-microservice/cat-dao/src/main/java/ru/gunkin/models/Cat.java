package ru.gunkin.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cats")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private Breed breed;
    private Color color;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Cat> friends;

    @NotNull
    private Long ownerId;
}

