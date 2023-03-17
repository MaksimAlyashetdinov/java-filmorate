package ru.yandex.practicum.filmorate.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UsersFriendsStorage;

@Repository
public class UsersFriendsDbStorage implements UsersFriendsStorage {

    private final JdbcTemplate jdbcTemplate;

    public UsersFriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addToFriends(int id, int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("User not found.");
        }
        try {
            String sqlQuery = "insert into Users_friends (proposer_id, invited_id) values (?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery);
                stmt.setLong(1, id);
                stmt.setLong(2, friendId);
                return stmt;
            }, keyHolder);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<User> getAllFriends(int id) {
        List<User> allFriends;
        try {
            allFriends = this.jdbcTemplate.query(
                    "select * from Users where user_id in (select invited_id from Users_friends where proposer_id = ?)",
                    (resultSet, rowNum) -> {
                        User user = new User();
                        user.setId(Integer.parseInt(resultSet.getString("user_id")));
                        user.setEmail(resultSet.getString("email"));
                        user.setLogin(resultSet.getString("login"));
                        user.setName(resultSet.getString("name"));
                        user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                        return user;
                    }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
        return allFriends;
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {
        try {
            String sql = "delete from Users_friends where proposer_id = ? and invited_id = ?";
            jdbcTemplate.update(sql, id, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public List<User> mutualFriends(int id, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        List<User> userFriends;
        try {
            userFriends = this.jdbcTemplate.query(
                    "select * from Users where user_id in (select invited_id from Users_friends where proposer_id = ?)",
                    (resultSet, rowNum) -> {
                        User user = new User();
                        user.setId(Integer.parseInt(resultSet.getString("user_id")));
                        user.setEmail(resultSet.getString("email"));
                        user.setLogin(resultSet.getString("login"));
                        user.setName(resultSet.getString("name"));
                        user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                        return user;
                    }, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
        List<User> secondUserFriends;
        try {
            secondUserFriends = this.jdbcTemplate.query(
                    "select * from Users where user_id in (select invited_id from Users_friends where proposer_id = ?)",
                    (resultSet, rowNum) -> {
                        User user = new User();
                        user.setId(Integer.parseInt(resultSet.getString("user_id")));
                        user.setEmail(resultSet.getString("email"));
                        user.setLogin(resultSet.getString("login"));
                        user.setName(resultSet.getString("name"));
                        user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                        return user;
                    }, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }
        for (User user : userFriends) {
            if (secondUserFriends.contains(user)) {
                mutualFriends.add(user);
            }
        }
        return mutualFriends;
    }
}
