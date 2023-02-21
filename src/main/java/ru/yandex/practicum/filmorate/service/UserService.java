package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(int id, int friendId) {
        validate(id, friendId);
        Set<Integer> friendsUser = userStorage.getUser(id).getFriends();
        friendsUser.add(friendId);
        userStorage.getUser(id).setFriends(friendsUser);
        friendsUser = userStorage.getUser(friendId).getFriends();
        friendsUser.add(id);
        userStorage.getUser(friendId).setFriends(friendsUser);
    }

    public void deleteFromFriends(int id, int friendId) {
        validate(id, friendId);
        Set<Integer> friendsUser = userStorage.getUser(id).getFriends();
        friendsUser.remove(friendId);
        userStorage.getUser(id).setFriends(friendsUser);
        friendsUser = userStorage.getUser(friendId).getFriends();
        friendsUser.remove(id);
        userStorage.getUser(friendId).setFriends(friendsUser);
    }

    public List<User> getAllFriends(int id) {
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
        validate(id, friendId);
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

    private void validate(int userId, int user2Id) {
        if (!userStorage.getAllUsersId().contains(userId)) {
            throw new NotFoundException("user" + userId + "not found.");
        }
        if (!userStorage.getAllUsersId().contains(user2Id)) {
            throw new NotFoundException("user " + user2Id + " not found.");
        }
    }
}
