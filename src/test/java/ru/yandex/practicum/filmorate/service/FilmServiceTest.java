package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

class FilmServiceTest {

    /*private InMemoryFilmStorage inMemoryFilmStorage;
    private InMemoryUserStorage inMemoryUserStorage;
    private FilmService filmService;

    @BeforeEach
    public void start() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        inMemoryUserStorage = new InMemoryUserStorage();
        filmService = new FilmService();
    }

    @Test
    public void addLike() {
        Film film = createFilm();
        inMemoryFilmStorage.addNewFilm(film);
        User user = createUser();
        inMemoryUserStorage.createUser(user);
        filmService.addLike(film, user);

        assertEquals(1, film.getLikes().size(),
                "The number of likes does not match the expected.");
    }

    @Test
    public void deleteLike() {
        Film film = createFilm();
        inMemoryFilmStorage.addNewFilm(film);
        User user = createUser();
        inMemoryUserStorage.createUser(user);
        filmService.addLike(film, user);

        assertEquals(1, film.getLikes().size(),
                "The number of likes does not match the expected.");

        filmService.deleteLike(film, user);

        assertEquals(0, film.getLikes().size(),
                "The number of likes does not match the expected.");
    }

    @Test
    public void bestFilms () {
        for (int i = 0; i < 15; i++) {
            Film film = createFilm();
            film.setName("Test film" + i);
            inMemoryFilmStorage.addNewFilm(film);
            User user = createUser();
            user.setName("Test user" + i);
            inMemoryUserStorage.createUser(user);
        }
        List<User> allUsers = inMemoryUserStorage.getAllUsers();
        List<Film> allFilms = inMemoryFilmStorage.getAllFilms();
        filmService.addLike(allFilms.get(5), allUsers.get(2));
        filmService.addLike(allFilms.get(5), allUsers.get(7));
        filmService.addLike(allFilms.get(5), allUsers.get(1));
        filmService.addLike(allFilms.get(8), allUsers.get(3));
        filmService.addLike(allFilms.get(3), allUsers.get(5));
        filmService.addLike(allFilms.get(3), allUsers.get(6));

        List<Film> bestFilms = filmService.bestFilms(inMemoryFilmStorage.getAllFilms());
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
    }*/
}