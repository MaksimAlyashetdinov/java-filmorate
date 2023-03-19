package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import ru.yandex.practicum.filmorate.model.User;

public interface UsersFriendsStorage {

    void addToFriends(int id, int friendId);

    List<User> getAllFriends(int id);

    void deleteFromFriends(int id, int friendId);

    List<User> mutualFriends(int id, int friendId);
}
