package ru.yandex.practicum.filmorate.dao;

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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class LikesDbStorageTest {

    private final LikesStorage likesStorage;
    private final UserStorage userStorage;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmStorage filmStorage;

    @Test
    void likeFilm() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = new Film(1, "Test_film", "Test_description", LocalDate.of(2023, 1, 1), 90,
                1, new HashSet<>(), mpa, genres);
        filmStorage.addNewFilm(testFilm);

        Optional<Film> filmFromMemory = Optional.ofNullable(filmStorage.getFilm(1));

        userStorage.createUser(new User(1, "test@yandex.ru", "testLogin", "Maxim",
                LocalDate.of(1986, 07, 12)));

        Optional<User> userFromMemory = Optional.ofNullable(userStorage.getUser(1));

        likesStorage.likeFilm(1, 1);

        Optional<Film> filmWithLike = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmWithLike)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().size()).isEqualTo(1)
                );

        assertThat(filmWithLike)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().contains(1)).isEqualTo(true)
                );
    }

    @Test
    void deleteLikeFilm() {
        MPA mpa = mpaStorage.getMPA(1);
        List<Genre> genres = new ArrayList<>();
        genres.add(genreStorage.getGenreById(1));
        Film testFilm = new Film(1, "Test_film", "Test_description", LocalDate.of(2023, 1, 1), 90,
                1, new HashSet<>(), mpa, genres);
        filmStorage.addNewFilm(testFilm);

        Optional<Film> filmFromMemory = Optional.ofNullable(filmStorage.getFilm(1));

        userStorage.createUser(new User(1, "test@yandex.ru", "testLogin", "Maxim",
                LocalDate.of(1986, 07, 12)));

        Optional<User> userFromMemory = Optional.ofNullable(userStorage.getUser(1));

        likesStorage.likeFilm(1, 1);
        Optional<Film> filmWithLike = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmWithLike)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().size()).isEqualTo(1)
                );

        likesStorage.deleteLikeFilm(1, 1);

        Optional<Film> filmOptionalId1WithoutLikes = Optional.ofNullable(filmStorage.getFilm(1));

        assertThat(filmOptionalId1WithoutLikes)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film.getLikes().size()).isEqualTo(0)
                );
    }
}

