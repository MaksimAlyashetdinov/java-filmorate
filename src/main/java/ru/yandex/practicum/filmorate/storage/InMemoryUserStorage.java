package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) {
        validate(user);
        setNameIfEmpty(user);
        user.setId(nextId());
        users.put(user.getId(), user);
        log.info("User added: " + user);
        return user;
    }

    public User updateUser(User user) {
        validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("The user with the specified id was not found.");
        }
        setNameIfEmpty(user);
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Total in the list of {} users.", allUsers.size());
        return allUsers;
    }

    private int nextId() {
        return ++id;
    }

    private void validate(User user) {

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
