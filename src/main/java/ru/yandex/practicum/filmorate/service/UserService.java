package ru.yandex.practicum.filmorate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@Service
public class UserService {

    public void addToFriends(User user, User user2) {
        Set<Integer> friendsUser = user.getFriends();
        friendsUser.add(user2.getId());
        user.setFriends(friendsUser);
        friendsUser = user2.getFriends();
        friendsUser.add(user.getId());
        user2.setFriends(friendsUser);
    }

    public void deleteFromFriends(User user, User user2) {
        Set<Integer> friendsUser = user.getFriends();
        friendsUser.remove(user2.getId());
        user.setFriends(friendsUser);
        friendsUser = user2.getFriends();
        friendsUser.remove(user.getId());
        user2.setFriends(friendsUser);
    }

    public List<Integer> mutualFriends (User user, User user2) {
        List<Integer> mutualFriends = new ArrayList<>();
        Set<Integer> friendsUser = user.getFriends();
        Set<Integer> friendsUser2 = user2.getFriends();
        for (int id : friendsUser) {
            if (friendsUser2.contains(id)) {
                mutualFriends.add(id);
            }
        }
        return mutualFriends;
    }
}
