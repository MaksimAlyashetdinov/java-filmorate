package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

   InMemoryFilmStorage inMemoryFilmStorage;

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.addNewFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        inMemoryFilmStorage.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }
}