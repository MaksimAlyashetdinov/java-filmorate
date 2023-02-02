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
    Map<Integer, User> users = new HashMap<>();

    private int nextId() {
        return ++id;
    }

    private void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Добавлен пользователь с незаполненным полем имя" + user);
        }

        if (user.getId() != 0 && !users.containsKey(user.getId())) {
            log.warn("Попытка добавить пользователя с недопустимым id" + user);
            throw new ValidationException("Пользователь с указанным id не найден.");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("Попытка добавить пользователя с недопустимым логином " + user);
            throw new ValidationException(
                    "Введен недопустимый логин. Логин не может быть пустым и содержать пробелы.");
        }
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        user.setId(nextId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь : " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        validate(user);
        if (user.getId() == 0) {
            user.setId(nextId());
            users.put(user.getId(), user);
            log.info("Добавлен пользователь : " + user);
        } else {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь : " + user);
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        log.info("Всего в списке {} пользователей.", allUsers.size());
        return allUsers;
    }
}
