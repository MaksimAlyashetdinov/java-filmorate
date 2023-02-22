package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    public Film addNewFilm(Film film) {
        film.setId(nextId());
        films.put(film.getId(), film);
        log.info("Added a movie: " + film);
        return film;
    }

    public Film updateFilm(Film film) {
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
        log.info("Get {} movie.", id);
        return films.get(id);
    }

    private int nextId() {
        return ++id;
    }
}
