package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateLogin(user);
        setNameIfEmpty(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user.getId());
        validateLogin(user);
        setNameIfEmpty(user);
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int id) {
        validate(id);
        return userStorage.getUser(id);
    }

    public void addToFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        Set<Integer> friendsUser = userStorage.getUser(id).getFriends();
        friendsUser.add(friendId);
        userStorage.getUser(id).setFriends(friendsUser);
        friendsUser = userStorage.getUser(friendId).getFriends();
        friendsUser.add(id);
        userStorage.getUser(friendId).setFriends(friendsUser);
    }

    public void deleteFromFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        Set<Integer> friendsUser = userStorage.getUser(id).getFriends();
        friendsUser.remove(friendId);
        userStorage.getUser(id).setFriends(friendsUser);
        friendsUser = userStorage.getUser(friendId).getFriends();
        friendsUser.remove(id);
        userStorage.getUser(friendId).setFriends(friendsUser);
    }

    public List<User> getAllFriends(int id) {
        validate(id);
        Set<Integer> friendsId = userStorage.getUser(id).getFriends();
        List<User> allFriends = new ArrayList<>();
        if (friendsId.isEmpty()) {
            return allFriends;
        }
        for (int friendId : friendsId) {
            User user = userStorage.getUser(friendId);
            allFriends.add(user);
        }
        return allFriends;
    }

    public List<User> mutualFriends(int id, int friendId) {
        validate(id);
        validate(friendId);
        List<User> mutualFriends = new ArrayList<>();
        Set<Integer> friendsUser = userStorage.getUser(id).getFriends();
        Set<Integer> friendsUser2 = userStorage.getUser(friendId).getFriends();
        for (int mutualId : friendsUser) {
            if (friendsUser2.contains(mutualId)) {
                mutualFriends.add(userStorage.getUser(mutualId));
            }
        }
        return mutualFriends;
    }

    private void validate(int userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("User" + userId + "not found.");
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
