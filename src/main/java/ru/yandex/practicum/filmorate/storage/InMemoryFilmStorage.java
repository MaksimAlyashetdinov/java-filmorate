package ru.yandex.practicum.filmorate.storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate RELEASE_DATA_OF_THE_OLDEST_MOVIE = LocalDate.of(1895, 12, 28);
    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    public Film addNewFilm(Film film) {
        validate(film);
        film.setId(nextId());
        films.put(film.getId(), film);
        log.info("Added a movie: " + film);
        return film;
    }

    public Film updateFilm(Film film) {
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("The movie with the specified id was not found.");
        }
        films.put(film.getId(), film);
        log.info("Updated movie: " + film);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>(films.values());
        log.info("Total in the list of {} movies.", allFilms.size());
        return allFilms;
    }

    public Film getFilm(int id) {
        return films.get(id);
    }

    private int nextId() {
        return ++id;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA_OF_THE_OLDEST_MOVIE)) {
            throw new ValidationException("The release date is not earlier than December 28, 1895.");
        }
    }
}
