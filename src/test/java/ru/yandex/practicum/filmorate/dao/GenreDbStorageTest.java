package ru.yandex.practicum.filmorate.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDbStorageTest {

    private final GenreStorage genreStorage;

    @Test
    void getGenresTest() {
        List<Genre> genres = genreStorage.getGenres();
        assertEquals(6, genres.size(), "The number of genres does not match.");
    }

    @Test
    void getGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreById(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }
}