package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        film.setDuration(90);
        return film;
    }

    @Test
    public void addFilmWithFullInformation() {
        Film film = createFilm();
        filmController.addNewFilm(film);

        assertEquals(1, filmController.getAllFilms().size(),
                "The number of films does not match the expected");

        Film filmForCheck = filmController.getFilm(1);

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
    public void addFilmWithWrongReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException e = assertThrows(ValidationException.class,
                () -> filmController.addNewFilm(film));
        assertEquals("The release date is not earlier than December 28, 1895.", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(),
                "The number of films does not match the expected");
    }

    @Test
    public void updateFilmTest() {
        Film film = createFilm();
        filmController.addNewFilm(film);
        Film updateFilm = filmController.getFilm(1);
        updateFilm.setName("Update_name");
        updateFilm.setDescription("Update description");
        updateFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateFilm.setDuration(1);
        filmController.updateFilm(updateFilm);
        Film filmForCheck = filmController.getFilm(1);

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
    public void updateFilmWithWrongId() {
        Film film = createFilm();
        film.setId(100);

        ValidationException e = assertThrows(ValidationException.class,
                () -> filmController.updateFilm(film));
        assertEquals("The movie with the specified id was not found.", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(),
                "The number of films does not match the expected");
    }

    @Test
    public void getAllFilmsTest() {
        Film film = createFilm();
        filmController.addNewFilm(film);
        Film film2 = createFilm();
        filmController.addNewFilm(film2);

        assertEquals(2, filmController.getAllFilms().size(),
                "The number of films does not match the expected");
    }
}