package ru.yandex.practicum.filmorate.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addNewFilm(Film film) {
        String sqlQuery =
                "insert into Films (film_name, description, release_date, duration, rate, mpa_id)" +
                        "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        return Optional.ofNullable(keyHolder.getKey())
                .map(Number::longValue)
                .map(id -> {
                    film.setId(Math.toIntExact(id));
                    if (film.getGenres() != null) {
                        setGenres(film, keyHolder);
                    }
                    return film;
                }).orElse(null);
    }

    @Override
    public List<Film> getAllFilms() {
        String filmsTableQuery = "select " +
                "f.film_id," +
                "f.film_name," +
                "f.description," +
                "f.release_date," +
                "f.duration," +
                "f.rate," +
                "f.mpa_id," +
                "m.mpa_name " +
                "from Films f " +
                "inner join MPA m on m.mpa_id = f.mpa_id";
        List<Film> filmsFromFilmsTable = jdbcTemplate.query(filmsTableQuery,
                FilmDbStorage::makeFilm);
        for (Film film : filmsFromFilmsTable) {
            if (film != null) {
                String findGenresForFilmQuery =
                        "select fg.genre_id, g.genre_name from Film_genre fg " +
                                "inner join Genres g on fg.genre_id = g.genre_id where fg.film_id = ?";
                List<Genre> foundGenresForFilm = jdbcTemplate.query(findGenresForFilmQuery,
                        new Object[]{film.getId()},
                        new int[]{Types.INTEGER},
                        (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                                rs.getString("genre_name")));
                film.setGenres(foundGenresForFilm);
            }
        }
        return filmsFromFilmsTable;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update Films " +
                "SET film_name = ?, " +
                "description = ?," +
                "release_date = ?," +
                "duration = ?," +
                "rate = ?," +
                "mpa_id = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(),
                film.getId());
        if (film.getGenres() != null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            setGenres(film, keyHolder);
        }
        return film;
    }

    private Film setGenres(Film film, KeyHolder keyHolder) {
        if (film.getGenres().isEmpty()) {
            String filmDeleteGenresQuery = "delete from Film_genre where film_id = ?";
            jdbcTemplate.update(filmDeleteGenresQuery, film.getId());
            return film;
        }
        String filmDeleteGenresQuery = "delete from Film_genre where film_id = ?";
        jdbcTemplate.update(filmDeleteGenresQuery, film.getId());

        String sqlQueryForFilmsToGenres = "insert into Film_genre (film_id, genre_id)" +
                "values (?, ?)";
        List<Genre> genresWithoutDuplicate = film.getGenres().stream().distinct().collect(
                Collectors.toList());
        for (Genre genre : genresWithoutDuplicate) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQueryForFilmsToGenres);
                stmt.setLong(1, film.getId());
                stmt.setLong(2, genre.getId());
                return stmt;
            }, keyHolder);
        }
        Film filmToSetGenres = film;
        filmToSetGenres.setGenres(genresWithoutDuplicate);
        return filmToSetGenres;
    }

    @Override
    public Film getFilm(int filmId) {
        String findFilmByIdQuery = "select " +
                "f.film_id," +
                "f.film_name," +
                "f.description," +
                "f.release_date," +
                "f.duration," +
                "f.rate," +
                "f.mpa_id," +
                "m.mpa_name from Films f " +
                "inner join MPA m on m.mpa_id = f.mpa_id " +
                "where f.film_id = ?";
        Film foundFilm = jdbcTemplate.queryForObject(findFilmByIdQuery, FilmDbStorage::makeFilm,
                filmId);
        if (foundFilm != null) {
            String findGenresForFilmQuery = "select fg.genre_id, g.genre_name from Film_genre fg " +
                    "inner join Genres g on fg.genre_id = g.genre_id where fg.film_id = ?";
            List<Genre> foundGenresForFilm = jdbcTemplate.query(findGenresForFilmQuery,
                    new Object[]{filmId},
                    new int[]{Types.INTEGER},
                    (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            foundFilm.setGenres(foundGenresForFilm);
            String findLikesForFilmQuery = "select user_id from Film_likes  " +
                    "where film_id = ?";
            List<Integer> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                    new Object[]{filmId},
                    new int[]{Types.INTEGER},
                    (rs, rowNum) -> rs.getInt("user_id"));
            Set<Integer> foundLikesForFilmSet = new HashSet<>(foundLikesForFilm);
            foundFilm.setLikes(foundLikesForFilmSet);
        }
        return foundFilm;
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> listOfGenres = new ArrayList<>();
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(listOfGenres)
                .build();
    }
}
