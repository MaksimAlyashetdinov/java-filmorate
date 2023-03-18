package ru.yandex.practicum.filmorate.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@Slf4j
public class FilmService {

    private static final LocalDate RELEASE_DATA_OF_THE_OLDEST_MOVIE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage,
            LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
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
        Film film = filmStorage.getFilm(id);
        Set<Integer> filmLikes = likesStorage.likeFilm(id, userId);
        filmLikes.add(userId);
        film.setLikes(filmLikes);
        film.setRate(film.getRate() + 1);
        log.info("Пользователь с ID {} поставил лайк фильму {} с ID {}", userId, film.getName(),
                id);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(int id, int userId) {
        validateByFilmId(id);
        validateByUserId(userId);
        Film film = filmStorage.getFilm(id);
        checkLikesCount(film);
        likesStorage.deleteLikeFilm(id, userId);
        Set<Integer> likes = film.getLikes();
        film.setLikes(likes);
        filmStorage.updateFilm(film);
        film.setRate(film.getRate() - 1);
        log.info("Пользователь с ID {} удалил лайк фильму {} с ID {}", userId, film.getName(), id);
    }

    public List<Film> bestFilms(int count) {
        log.info("Get best {} films", count);
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getRate() - o1.getRate())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateByFilmId(int id) {
        try {
            if (filmStorage.getFilm(id) == null) {
                throw new NotFoundException(String.format("Film %d not found.", id));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Film %d not found.", id));
        }
    }

    private void validateByUserId(int userId) {
        try {
            if (userStorage.getUser(userId) == null) {
                throw new NotFoundException(String.format("User %d not found.", userId));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("User %d not found.", userId));
        }
    }

    private void validateByReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATA_OF_THE_OLDEST_MOVIE)) {
            throw new ValidationException(
                    "The release date is not earlier than December 28, 1895.");
        }
    }

    private Film checkLikesCount(Film film) {
        if (film.getRate() <= 0) {
            log.info(
                    "You can't remove a movie's like from ID {}, since the number of likes is zero",
                    film.getId());
            throw new NotFoundException("The number of likes is zero");
        }
        return film;
    }
}