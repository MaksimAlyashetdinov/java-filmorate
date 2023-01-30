package ru.yandex.practicum.filmorate.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

@RestController("/users")
@Slf4j
public class UserController {

    private int id;
    Map<Integer, User> users = new HashMap<>();

    private int nextId() {
        return ++id;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Введен недопустимый email.");
            }
            if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                throw new ValidationException(
                        "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.");
            }
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть позже текущей даты.");
            }
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь : " + user.toString());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            log.warn(e.getMessage());
            return user;
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Введен недопустимый email.");
            }
            if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                throw new ValidationException(
                        "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.");
            }
            if (user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть позже текущей даты.");
            }
            if (user.getId() == 0) {
                user.setId(nextId());
                users.put(user.getId(), user);
                log.info("Добавлен пользователь : " + user.toString());
            }
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Обновлен пользователь : " + user.toString());
            } else {
                throw new ValidationException("Пользователь с таким id не найден.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            log.warn(e.getMessage());
            return user;
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>(users.values());
        log.info("Всего в списке {} пользователей.", usersList.size());
        return usersList;
    }
}
