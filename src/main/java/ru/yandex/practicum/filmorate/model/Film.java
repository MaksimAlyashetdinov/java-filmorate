package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "You must enter the name of the movie.")
    private String name;

    @Size(max = 200, message = "The maximum length of the description is 200 characters.")
    private String description;

    @NotNull
    @Past
    private LocalDate releaseDate;

    @Positive(message = "The duration of the film should be positive.")
    private int duration;

    private int rate;

    private Set<Integer> likes;

    private MPA mpa;

    private List<Genre> genres;
}