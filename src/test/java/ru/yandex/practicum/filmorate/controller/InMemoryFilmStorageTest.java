package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

class InMemoryFilmStorageTest {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void createController() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
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
        inMemoryFilmStorage.addNewFilm(film);
        List<Film> allFilms = inMemoryFilmStorage.getAllFilms();

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
    public void addFilmWithWrongReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException e = assertThrows(ValidationException.class,
                () -> inMemoryFilmStorage.addNewFilm(film));
        assertEquals("The release date is not earlier than December 28, 1895.", e.getMessage());
        assertEquals(0, inMemoryFilmStorage.getAllFilms().size(),
                "The number of films does not match the expected.");
    }

    @Test
    public void updateFilmTest() {
        Film film = createFilm();
        inMemoryFilmStorage.addNewFilm(film);
        List<Film> allFilms = inMemoryFilmStorage.getAllFilms();
        Film updateFilm = allFilms.get(0);
        updateFilm.setName("Update_name");
        updateFilm.setDescription("Update description");
        updateFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        updateFilm.setDuration(1);
        inMemoryFilmStorage.updateFilm(updateFilm);
        List<Film> allFilmsAfterUpdate = inMemoryFilmStorage.getAllFilms();
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
                () -> inMemoryFilmStorage.updateFilm(film));
        assertEquals("The movie with the specified id was not found.", e.getMessage());
        assertEquals(0, inMemoryFilmStorage.getAllFilms().size(),
                "The number of films does not match the expected.");
    }

    @Test
    public void getAllFilmsTest() {
        Film film = createFilm();
        inMemoryFilmStorage.addNewFilm(film);
        Film film2 = createFilm();
        inMemoryFilmStorage.addNewFilm(film2);

        assertEquals(2, inMemoryFilmStorage.getAllFilms().size(),
                "The number of films does not match the expected.");
    }
}