package ru.yandex.practicum.filmorate.controllers;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.yandex.practicum.filmorate.model.User;

public class UserControllerTest {

    UserController userController;

    @BeforeEach
    private void createController() {
        userController = new UserController();
    }

    @Test
    private void createUserWithFullInformation() {
        User user = new User();
        user.setName("Test name");
        user.setLogin("Test login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        userController.createUser(user);

        assertEquals(1, userController.users.size(), "The number of users does not match the expected");

        User userForCheck = userController.users.get(1);

        assertEquals(user.getName(), userForCheck.getName(), "The user names don't match");
        assertEquals(user.getLogin(), userForCheck.getLogin(), "The user logins don't match");
        assertEquals(user.getEmail(), userForCheck.getEmail(), "The user emails don't match");
        assertEquals(user.getBirthday(), userForCheck.getBirthday(), "The user birthdays don't match");
    }

    @Test
    private void createUserWithoutName() {
        User user = new User();
        user.setLogin("Test login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        userController.createUser(user);

        assertEquals(1, userController.users.size(), "The number of users does not match the expected");

        User userForCheck = userController.users.get(1);

        assertEquals(user.getLogin(), userForCheck.getName(), "The user names don't match");
    }
}
