package ru.yandex.practicum.filmorate.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
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

    public List<Film> bestFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(int id, int userId) {
        if (!filmStorage.getAllFilmsId().contains(id)) {
            throw new NotFoundException("Film " + id + " not found.");
        }
        if (!userStorage.getAllUsersId().contains(userId)) {
            throw new NotFoundException("User" + userId + " not found.");
        }
    }
}
