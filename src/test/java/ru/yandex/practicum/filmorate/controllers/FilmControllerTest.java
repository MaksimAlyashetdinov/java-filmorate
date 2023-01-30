package ru.yandex.practicum.filmorate.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    public void createController() {
        filmController = new FilmController();
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("Test_film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        film.setDuration(Duration.ofMinutes(90));
        return film;
    }


    @Test
    public void addFilmWithFullInformation() {
        Film film = createFilm();
        filmController.addNewFilm(film);

        assertEquals(1, filmController.films.size(),
                "The number of films does not match the expected");

        Film filmForCheck = filmController.films.get(1);

        assertEquals(film.getName(), filmForCheck.getName(),
                "The film names don't match");
        assertEquals(film.getDescription(), filmForCheck.getDescription(),
                "The film descriptions don't match");
        assertEquals(film.getReleaseDate(), filmForCheck.getReleaseDate(),
                "The film releaseDate don't match");
        assertEquals(film.getDuration(), filmForCheck.getDuration(),
                "The film duration don't match");
    }

    @Test
    public void addFilmWithoutName() {
        Film film = createFilm();
        film.setName("");
        filmController.addNewFilm(film);

        assertEquals(0, filmController.films.size(),
                "The number of films does not match the expected");
    }

    @Test
    public void addFilmWithWrongDescription() {
        Film film = createFilm();
        film.setDescription("Description is too long: ........................................"
                + "............................................................................"
                + "............................................................................"
                + "............................................................................");
        filmController.addNewFilm(film);

        assertEquals(0, filmController.films.size(), "The number of films does not match the expected");
    }

    @Test
    public void addFilmWithWrongReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        filmController.addNewFilm(film);

        assertEquals(0, filmController.films.size(), "The number of films does not match the expected");
    }

    @Test
    public void addFilmWithWrongDuration() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-1));
        filmController.addNewFilm(film);

        assertEquals(0, filmController.films.size(), "The number of films does not match the expected");
    }

    @Test
    public void updateFilmTest() {
        Film film = createFilm();
        filmController.addNewFilm(film);
        Film updateFilm = filmController.films.get(1);
        updateFilm.setName("Update_name");
        updateFilm.setDescription("Update description");
        updateFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateFilm.setDuration(Duration.ofMinutes(1));
        filmController.updateFilm(updateFilm);
        Film filmForCheck = filmController.films.get(1);

        assertEquals(updateFilm.getName(), filmForCheck.getName(),
                "The film names don't match");
        assertEquals(updateFilm.getDescription(), filmForCheck.getDescription(),
                "The film descriptions don't match");
        assertEquals(updateFilm.getReleaseDate(), filmForCheck.getReleaseDate(),
                "The film releaseDate don't match");
        assertEquals(updateFilm.getDuration(), filmForCheck.getDuration(),
                "The film duration don't match");
    }

    @Test
    public void getAllFilmsTest() {
        Film film = createFilm();
        filmController.addNewFilm(film);
        Film film2 = createFilm();
        filmController.addNewFilm(film2);

        assertEquals(2, filmController.getAllFilms().size(), "The number of films does not match the expected");
    }
}