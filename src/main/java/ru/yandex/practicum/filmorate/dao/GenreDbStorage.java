package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> allGenres = this.jdbcTemplate.query(
                "select genre_id, genre_name from Genres",
                (resultSet, rowNum) -> {
                    Genre genre = new Genre(resultSet.getInt("genre_id"),
                            resultSet.getString("genre_name"));
                    return genre;
                });
        return allGenres.stream().sorted((o1, o2) -> o1.getId() - o2.getId())
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(int id) {
        Genre genre = jdbcTemplate.queryForObject(
                "select genre_id, genre_name from Genres where genre_id = ?",
                (resultSet, rowNum) -> {
                    Genre newGenre = new Genre(resultSet.getInt("genre_id"),
                            resultSet.getString("genre_name"));
                    return newGenre;
                },
                id);
        return genre;
    }
}
