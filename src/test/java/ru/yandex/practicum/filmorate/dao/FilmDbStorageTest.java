package ru.yandex.practicum.filmorate.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Test
    void addNewFilmTest() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = createTestFilm();
        filmStorage.addNewFilm(testFilm);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Test_film_name")
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description",
                                "Test_description")
                );

        LocalDate testDate = LocalDate.of(2023, 1, 1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", testDate)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 90)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getMpa()).isEqualTo(mpa)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getGenres()).isEqualTo(genres)
                );
    }

    @Test
    void getAllFilmsTest() {
        Film film1 = createTestFilm();
        filmStorage.addNewFilm(film1);
        Film film2 = createTestFilm();
        film2.setId(2);
        filmStorage.addNewFilm(film2);
        List<Film> allFilms = filmStorage.getAllFilms();

        assertEquals(2, allFilms.size(), "The number of films does not match.");
    }

    @Test
    void updateFilmTest() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = createTestFilm();
        filmStorage.addNewFilm(testFilm);
        Film updateFilm = new Film(1, "Update_name", "Update_description",
                LocalDate.of(2022, 12, 31), 110, 1, new HashSet<>(), mpa, genres);
        filmStorage.updateFilm(updateFilm);
        Optional<Film> filmOptionalId1Update = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Update_name")
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description",
                                "Update_description")
                );

        assertThat(filmOptionalId1Update)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 110)
                );
    }

    @Test
    void getFilmTest() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = createTestFilm();
        filmStorage.addNewFilm(testFilm);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Test_film_name")
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description",
                                "Test_description")
                );

        LocalDate testDate = LocalDate.of(2023, 1, 1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", testDate)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 90)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("rate", 0)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getMpa()).isEqualTo(mpa)
                );

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getGenres()).isEqualTo(genres)
                );
    }

    private Film createTestFilm() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = new Film(1, "Test_film_name", "Test_description", LocalDate.of(2023, 1, 1),
                90, 0, new HashSet<>(), mpa, genres);
        return testFilm;
    }
}