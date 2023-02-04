package ru.yandex.practicum.filmorate.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int id;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validate(user);
        user.setId(nextId());
        users.put(user.getId(), user);
        log.info("User added: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validate(user);
        if (user.getId() == 0) {
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("User added: " + user);
        } else {
            users.put(user.getId(), user);
            log.info("User updated: " + user);
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Total in the list of {} users.", allUsers.size());
        return allUsers;
    }

    public User getUser(int id) {
        return users.get(id);
    }

    private int nextId() {
        return ++id;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Added a user with an empty name field" + user);
        }

        if (user.getId() != 0 && !users.containsKey(user.getId())) {
            throw new ValidationException("The user with the specified id was not found.");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException(
                    "An invalid login has been entered. The login cannot be empty and contain spaces.");
        }
    }
}
