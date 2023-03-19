package ru.yandex.practicum.filmorate.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
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
        String sqlQuery = "insert into Users_friends (user_id, friend_id) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setLong(1, id);
            stmt.setLong(2, friendId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public List<User> getAllFriends(int id) {
        List<User> allFriends = this.jdbcTemplate.query(
                "select * from Users where user_id in (select friend_id from Users_friends where user_id = ?)",
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("user_id")));
                    user.setEmail(resultSet.getString("email"));
                    user.setLogin(resultSet.getString("login"));
                    user.setName(resultSet.getString("name"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                    return user;
                }, id);
        return allFriends;
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {
        String sql = "delete from Users_friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    public List<User> mutualFriends(int id, int friendId) {
        List<User> mutualFriends = new ArrayList<>();
        List<User> userFriends = this.jdbcTemplate.query(
                "select * from Users where user_id in (select friend_id from Users_friends where user_id = ?)",
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("user_id")));
                    user.setEmail(resultSet.getString("email"));
                    user.setLogin(resultSet.getString("login"));
                    user.setName(resultSet.getString("name"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                    return user;
                }, id);

        List<User> secondUserFriends = this.jdbcTemplate.query(
                "select * from Users where user_id in (select friend_id from Users_friends where user_id = ?)",
                (resultSet, rowNum) -> {
                    User user = new User();
                    user.setId(Integer.parseInt(resultSet.getString("user_id")));
                    user.setEmail(resultSet.getString("email"));
                    user.setLogin(resultSet.getString("login"));
                    user.setName(resultSet.getString("name"));
                    user.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                    return user;
                }, friendId);
        for (User user : userFriends) {
            if (secondUserFriends.contains(user)) {
                mutualFriends.add(user);
            }
        }
        return mutualFriends;
    }
}
