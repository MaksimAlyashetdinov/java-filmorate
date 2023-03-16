package ru.yandex.practicum.filmorate.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "insert into Users (email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update Users set " +
                "email = ?, login = ?, name = ?, birthday = ?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = this.jdbcTemplate.query(
                "select user_id, email, login, name, birthday from Users",
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("user_id")));
                    user.setEmail(resultSet.getString("email"));
                    user.setLogin(resultSet.getString("login"));
                    user.setName(resultSet.getString("name"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                    return user;
                });
        return allUsers;
    }

    @Override
    public User getUser(int id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(
                    "select user_id, email, login, name, birthday from Users where user_id = ?",
                    (resultSet, rowNum) -> {
                        User newUser = new User();
                        newUser.setId(Integer.parseInt(resultSet.getString("user_id")));
                        newUser.setEmail(resultSet.getString("email"));
                        newUser.setLogin(resultSet.getString("login"));
                        newUser.setName(resultSet.getString("name"));
                        newUser.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                        return newUser;
                    },
                    id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(e.getLocalizedMessage());
        }
        return user;
    }
}
