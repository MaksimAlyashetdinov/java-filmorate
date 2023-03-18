package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    int id;

    @NotBlank(message = "You must enter the name of the movie.")
    String name;

    @Size(max = 200, message = "The maximum length of the description is 200 characters.")
    String description;

    @NotNull
    @Past
    LocalDate releaseDate;

    @Positive(message = "The duration of the film should be positive.")
    int duration;

    int rate;

    Set<Integer> likes;

    MPA mpa;

    List<Genre> genres;
}