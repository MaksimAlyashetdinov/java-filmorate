package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

class FilmServiceTest {

    private FilmService filmService;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @BeforeEach
    public void start() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
    }

    @Test
    public void addLike() {
        Film film = createFilm();
        filmStorage.addNewFilm(film);
        User user = createUser();
        userStorage.createUser(user);
        filmService.addLike(film.getId(), user.getId());

        assertEquals(1, film.getLikes().size(),
                "The number of likes does not match the expected.");
    }

    @Test
    public void deleteLike() {
        Film film = createFilm();
        filmStorage.addNewFilm(film);
        User user = createUser();
        userStorage.createUser(user);
        filmService.addLike(film.getId(), user.getId());

        assertEquals(1, film.getLikes().size(),
                "The number of likes does not match the expected.");

        filmService.deleteLike(film.getId(), user.getId());

        assertEquals(0, film.getLikes().size(),
                "The number of likes does not match the expected.");
    }

    @Test
    public void bestFilms () {
        for (int i = 0; i < 10; i++) {
            Film film = createFilm();
            film.setName("Test film" + i);
            filmStorage.addNewFilm(film);
            User user = createUser();
            user.setName("Test user" + i);
            userStorage.createUser(user);
        }

        filmService.addLike(5, 2);
        filmService.addLike(5, 7);
        filmService.addLike(5, 1);
        filmService.addLike(8, 3);
        filmService.addLike(3, 5);
        filmService.addLike(3, 6);

        List<Film> bestFilms = filmService.bestFilms(3);
        assertEquals(3, bestFilms.size(),
                "The number of films in list does not match the expected.");
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("Test_film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(90);
        return film;
    }

    private User createUser() {
        User user = new User();
        user.setName("Test_name");
        user.setLogin("Test_login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        return user;
    }
}