package ru.yandex.practicum.filmorate.dao;

import java.util.Set;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UsersFriendsStorage;

public class UsersFriendsDbStorage implements UsersFriendsStorage {

    @Override
    public void addFriend(int id, int friendId) {

    }

    @Override
    public Set<User> getFriends(int id) {
        return null;
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {

    }
}
