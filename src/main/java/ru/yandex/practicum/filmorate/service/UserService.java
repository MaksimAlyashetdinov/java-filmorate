package ru.yandex.practicum.filmorate.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UsersFriendsDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final UsersFriendsDbStorage usersFriendsDbStorage;

    @Autowired
    public UserService(UserStorage userStorage, UsersFriendsDbStorage usersFriendsDbStorage) {
        this.userStorage = userStorage;
        this.usersFriendsDbStorage = usersFriendsDbStorage;
    }

    public User createUser(User user) {
        validateLogin(user);
        setNameIfEmpty(user);
        log.info("User added: " + user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user.getId());
        validateLogin(user);
        setNameIfEmpty(user);
        log.info("User updated: " + user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Total in the list of {} users.", userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        validate(id);
        log.info("Get {} user.", id);
        return userStorage.getUser(id);
    }

    public void addToFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        log.info("User {} add to friend user {}.", id, friendId);
        usersFriendsDbStorage.addToFriends(id, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        log.info("User {} delete from friends user {}.", id, friendId);
        usersFriendsDbStorage.deleteFromFriends(id, friendId);
    }

    public List<User> getAllFriends(int id) {
        validate(id);
        log.info("Get all friends for user {}", id);
        return usersFriendsDbStorage.getAllFriends(id);
    }

    public List<User> mutualFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        log.info("Get mutual friends for users {} and {}", id, friendId);
        return usersFriendsDbStorage.mutualFriends(id, friendId);
    }

    private void validate(int userId) {
        try {
            if (userStorage.getUser(userId) == null) {
                throw new NotFoundException("User" + userId + "not found.");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User " + userId + " not found.");
        }
    }

    private void validateLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException(
                    "An invalid login has been entered. The login cannot be empty and contain spaces.");
        }
    }

    private static void setNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Added a user with an empty name field" + user);
        }
    }
}
