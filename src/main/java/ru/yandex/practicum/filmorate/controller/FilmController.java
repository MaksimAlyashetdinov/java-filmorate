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

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private int id;
    Map<Integer, Film> films = new HashMap<>();

    private int nextId() {
        return ++id;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Попытка добавить фильм с недопустимой датой релиза" + film);
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }

        if (film.getId() != 0 && !films.containsKey(film.getId())) {
            log.warn("Попытка добавить фильм с недопустимым id" + film);
            throw new ValidationException("Фильм с указанным id не найден.");
        }
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        film.setId(nextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм : " + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        if (film.getId() == 0) {
            film.setId(nextId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм : " + film);
        } else {
            films.put(film.getId(), film);
            log.info("Обновлен фильм : " + film);
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>(films.values());
        log.info("Всего в списке {} фильмов.", allFilms.size());
        return allFilms;
    }
}