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
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    InMemoryUserStorage inMemoryUserStorage;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.createUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        inMemoryUserStorage.updateUser(user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }
}
