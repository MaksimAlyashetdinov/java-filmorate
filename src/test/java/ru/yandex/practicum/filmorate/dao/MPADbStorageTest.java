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
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class MPADbStorageTest {

    private final MPAStorage mpaStorage;

    @Test
    void getMPAs() {
        List<MPA> allMPAs = mpaStorage.getMPAs();
        assertEquals(5, allMPAs.size(), "The number of MPA does not match.");
    }

    @Test
    void getMPA() {
        Optional<MPA> genreOptional = Optional.ofNullable(mpaStorage.getMPA(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "G")
                );
    }
}