package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
        validate(id, userId);
        Set<Integer> filmLikes = new HashSet<>();
        if (!filmStorage.getFilm(id).getLikes().isEmpty()) {
            filmLikes = filmStorage.getFilm(id).getLikes();
        }
        filmLikes.add(userId);
        filmStorage.getFilm(id).setLikes(filmLikes);
    }

    public void deleteLike(int id, int userId) {
        validate(id, userId);
        Set<Integer> filmLikes = filmStorage.getFilm(id).getLikes();
        filmLikes.remove(userId);
        filmStorage.getFilm(id).setLikes(filmLikes);
    }

    public List<Film> bestFilms (int count) {
        List<Film> bestFilms = new ArrayList<>();
        for (Film film : filmStorage.getAllFilms()) {
            if (!film.getLikes().isEmpty()) {
                bestFilms.add(film);
            }
        }
        Collections.sort(bestFilms, new FilmComparator());
        Collections.reverse(bestFilms);
        if (bestFilms.size() >= count) {
            bestFilms = bestFilms.subList(0, count);
        }
        return bestFilms;
    }

    private void validate(int id, int userId) {
        if (!filmStorage.getAllFilmsId().contains(id)) {
            throw new NotFoundException("Film " + id + " not found.");
        }
        if (!userStorage.getAllUsers().contains(userId)) {
            throw new NotFoundException("User" + userId + " not found.");
        }
    }
}
