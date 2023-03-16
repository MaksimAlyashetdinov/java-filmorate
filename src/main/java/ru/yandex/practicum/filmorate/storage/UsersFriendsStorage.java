package ru.yandex.practicum.filmorate.storage;

import java.util.Set;
import ru.yandex.practicum.filmorate.model.User;

public interface UsersFriendsStorage {

    void addFriend(int id, int friendId);

    Set<User> getFriends(int id);

    void deleteFromFriends(int id, int friendId);
}
