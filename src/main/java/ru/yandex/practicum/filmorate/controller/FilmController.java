package ru.yandex.practicum.filmorate.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
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

    private int dataValidation(Film film) {
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
            if (film.getDuration() < 0) {
                throw new ValidationException("Продолжительность фильма должна быть положительной");
            }
        } catch (ValidationException e) {
            System.out.println(e);
            log.warn(e.getMessage());
            return Response.SC_BAD_REQUEST;
        }
        return Response.SC_OK;
    }

    @PostMapping
    public ResponseEntity addNewFilm(@Valid @RequestBody Film film) {
        if (dataValidation(film) == Response.SC_OK) {
            film.setId(nextId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм : " + film.toString());
            return ResponseEntity.status(Response.SC_OK).body(film);
        }
        return ResponseEntity.status(Response.SC_BAD_REQUEST).body(film);
    }

    @PutMapping
    public ResponseEntity updateFilm(@Valid @RequestBody Film film) {
        try {
            if (dataValidation(film) == Response.SC_OK) {
                if (film.getId() == 0) {
                    film.setId(nextId());
                    films.put(film.getId(), film);
                    log.info("Добавлен фильм : " + film.toString());
                    return ResponseEntity.status(Response.SC_OK).body(film);
                } else if (films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                    log.info("Обновлен фильм : " + film.toString());
                    return ResponseEntity.status(Response.SC_OK).body(film);
                } else {
                    throw new ValidationException("Фильм с таким id не найден");
                }
            }
        } catch (ValidationException e) {
            System.out.println(e);
            log.warn(e.getMessage());
            return ResponseEntity.status(Response.SC_NOT_FOUND).body(film);
        }
        return ResponseEntity.status(dataValidation(film)).body(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> filmsList = new ArrayList<>(films.values());
        log.info("Всего в списке {} фильмов.", filmsList.size());
        return filmsList;
    }
}