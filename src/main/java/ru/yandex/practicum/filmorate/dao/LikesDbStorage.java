package ru.yandex.practicum.filmorate.dao;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Component
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Integer> likeFilm(int id, int userId) {
        String sqlQueryForFilmsToGenres = "insert into Film_likes (film_id, user_id)" +
                "values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQueryForFilmsToGenres);
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);

        String findLikesForFilmQuery = "select user_id from Film_likes " +
                "where film_id = ?";

        List<Integer> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                new Object[]{userId},
                new int[]{Types.INTEGER},
                (rs, rowNum) -> rs.getInt("user_id"));
        Set<Integer> likes = new HashSet<>(foundLikesForFilm);
        return likes;
    }

    @Override
    public Set<Integer> deleteLikeFilm(int id, int userId) {
        String deleteLikesForFilmQuery = "delete from Film_likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(deleteLikesForFilmQuery, userId, id);

        String findLikesForFilmQuery = "select user_id from Film_likes " +
                "where film_id = ?";
        List<Integer> foundLikesForFilm = jdbcTemplate.query(findLikesForFilmQuery,
                new Object[]{userId},
                new int[]{Types.INTEGER},
                (rs, rowNum) -> rs.getInt("user_id"));
        Set<Integer> likes = new HashSet<>(foundLikesForFilm);
        return likes;
    }
}
