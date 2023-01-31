package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
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
    Map<Integer, User> users = new HashMap<>();

    private int nextId() {
        return ++id;
    }

    private int dataValidation(User user) {
        try {
            if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Введен недопустимый email.");
            }
            if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                throw new ValidationException(
                        "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.");
            }
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть позже текущей даты.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            log.warn(e.getMessage());
            return Response.SC_BAD_REQUEST;
        }
        return Response.SC_OK;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody User user) {
        if (dataValidation(user) == Response.SC_OK) {
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь : " + user.toString());
            return ResponseEntity.status(Response.SC_OK).body(user);
        }
        return ResponseEntity.status(Response.SC_BAD_REQUEST).body(user);
    }

    @PutMapping
    public ResponseEntity updateUser(@Valid @RequestBody User user) {
        try {
            if (dataValidation(user) == Response.SC_OK) {
                if (user.getId() == 0) {
                    user.setId(nextId());
                    users.put(user.getId(), user);
                    log.info("Добавлен пользователь : " + user.toString());
                    return ResponseEntity.status(Response.SC_OK).body(user);
                } else if (users.containsKey(user.getId())) {
                    users.put(user.getId(), user);
                    log.info("Обновлен пользователь : " + user.toString());
                    return ResponseEntity.status(Response.SC_OK).body(user);
                } else {
                    throw new ValidationException("Пользователь с таким id не найден.");
                }
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
            log.warn(e.getMessage());
            return ResponseEntity.status(Response.SC_NOT_FOUND).body(user);
        }
        return ResponseEntity.status(dataValidation(user)).body(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>(users.values());
        log.info("Всего в списке {} пользователей.", usersList.size());
        return usersList;
    }
}
