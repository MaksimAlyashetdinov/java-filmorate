package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@Slf4j
public class FilmService {

    private static final LocalDate RELEASE_DATA_OF_THE_OLDEST_MOVIE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addNewFilm(Film film) {
        validateByReleaseDate(film);
        log.info("Added a movie: " + film);
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film film) {
        validateByFilmId(film.getId());
        validateByReleaseDate(film);
        log.info("Updated movie: " + film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Total in the list of {} movies.", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        validateByFilmId(id);
        log.info("Get {} movie.", id);
        return filmStorage.getFilm(id);
    }

    public void addLike(int id, int userId) {
        validateByFilmId(id);
        validateByUserId(userId);
        Set<Integer> filmLikes = new HashSet<>();
        if (!filmStorage.getFilm(id).getLikes().isEmpty()) {
            filmLikes = filmStorage.getFilm(id).getLikes();
        }
        filmLikes.add(userId);
        filmStorage.getFilm(id).setLikes(filmLikes);
    }

    public void deleteLike(int id, int userId) {
        validateByFilmId(id);
        validateByUserId(userId);
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

    private void validateByFilmId(int id) {
        if (filmStorage.getFilm(id) == null) {
            throw new NotFoundException("Film " + id + " not found.");
        }
    }

    private void validateByUserId(int userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("User " + userId + " not found.");
        }
    }

    private void validateByReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA_OF_THE_OLDEST_MOVIE)) {
            throw new ValidationException(
                    "The release date is not earlier than December 28, 1895.");
        }
    }
}