package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Film {

    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private int duration;
}