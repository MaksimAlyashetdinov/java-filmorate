package ru.yandex.practicum.filmorate.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@RestController("/films")
@Slf4j
public class FilmController {

    private int id;
    Map<Integer, Film> films = new HashMap<>();


    private int nextId() {
        return ++id;
    }

    private boolean dataValidation(Film film) {
        try {
            if (film.getName().isBlank()) {
                throw new ValidationException("Необходимо ввести название фильма");
            }
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Максимальная длина описания — 200 символов");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            if (film.getDuration().isNegative()) {
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
        } catch (ValidationException e) {
            System.out.println(e);
            log.warn(e.getMessage());
            return false;
        }
        return true;
    }

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) {
        if (dataValidation(film)) {
            film.setId(nextId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм : " +film.toString());
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (dataValidation(film)) {
                if (film.getId() == 0) {
                    film.setId(nextId());
                    films.put(film.getId(), film);
                    log.info("Добавлен фильм : " + film.toString());
                } else if (films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                    log.info("Обновлен фильм : " + film.toString());
                } else {
                    throw new ValidationException("Фильм с таким id не найден");
                }
            }
        } catch (ValidationException e) {
            System.out.println(e);
            log.warn(e.getMessage());
            return film;
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Всего в списке {} фильмов.",filmsList.size());
        return filmsList;
    }
}
