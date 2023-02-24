package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    @Test
    public void addFilmWithWrongReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException e = assertThrows(ValidationException.class,
                () -> filmService.addNewFilm(film));
        assertEquals("The release date is not earlier than December 28, 1895.", e.getMessage());
        assertEquals(0, filmService.getAllFilms().size(),
                "The number of films does not match the expected.");
    }

    @Test
    public void addFilmWithFullInformation() {
        Film film = createFilm();
        filmService.addNewFilm(film);
        List<Film> allFilms = filmService.getAllFilms();

        assertEquals(1, allFilms.size(),
                "The number of films does not match the expected.");

        Film filmForCheck = allFilms.get(0);

        assertEquals(film.getName(), filmForCheck.getName(),
                "The film names don't match.");
        assertEquals(film.getDescription(), filmForCheck.getDescription(),
                "The film descriptions don't match.");
        assertEquals(film.getReleaseDate(), filmForCheck.getReleaseDate(),
                "The film releaseDate don't match.");
        assertEquals(film.getDuration(), filmForCheck.getDuration(),
                "The film duration don't match.");
    }



    @Test
    public void updateFilmTest() {
        Film film = createFilm();
        filmService.addNewFilm(film);
        List<Film> allFilms = filmService.getAllFilms();
        Film updateFilm = allFilms.get(0);
        updateFilm.setName("Update_name");
        updateFilm.setDescription("Update description");
        updateFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateFilm.setDuration(1);
        filmService.updateFilm(updateFilm);
        List<Film> allFilmsAfterUpdate = filmService.getAllFilms();
        Film filmForCheck = allFilmsAfterUpdate.get(0);

        assertEquals(updateFilm.getName(), filmForCheck.getName(),
                "The film names don't match.");
        assertEquals(updateFilm.getDescription(), filmForCheck.getDescription(),
                "The film descriptions don't match.");
        assertEquals(updateFilm.getReleaseDate(), filmForCheck.getReleaseDate(),
                "The film releaseDate don't match.");
        assertEquals(updateFilm.getDuration(), filmForCheck.getDuration(),
                "The film duration don't match.");
    }

    @Test
    public void updateFilmWithWrongId() {
        Film film = createFilm();
        film.setId(100);

        NotFoundException e = assertThrows(NotFoundException.class,
                () -> filmService.updateFilm(film));
        assertEquals("Film " + film.getId() + " not found.", e.getMessage());
        assertEquals(0, filmService.getAllFilms().size(),
                "The number of films does not match the expected.");
    }

    @Test
    public void getAllFilmsTest() {
        Film film = createFilm();
        filmService.addNewFilm(film);
        Film film2 = createFilm();
        filmService.addNewFilm(film2);

        assertEquals(2, filmService.getAllFilms().size(),
                "The number of films does not match the expected.");
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