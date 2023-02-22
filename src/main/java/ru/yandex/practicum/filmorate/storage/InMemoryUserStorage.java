package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) {
        user.setId(nextId());
        users.put(user.getId(), user);
        log.info("User added: " + user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Total in the list of {} users.", allUsers.size());
        return allUsers;
    }

    public User getUser(int id) {
        log.info("Get {} user.", id);
        return users.get(id);
    }

    private int nextId() {
        return ++id;
    }
}
