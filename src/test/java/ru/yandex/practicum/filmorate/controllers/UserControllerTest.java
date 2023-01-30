package ru.yandex.practicum.filmorate.controllers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void createController() {
        userController = new UserController();
    }

    private User createUser() {
        User user = new User();
        user.setName("Test_name");
        user.setLogin("Test_login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        return user;
    }


    @Test
    public void createUserWithFullInformation() {
        User user = createUser();
        userController.createUser(user);

        assertEquals(1, userController.users.size(), "The number of users does not match the expected");

        User userForCheck = userController.users.get(1);

        assertEquals(user.getName(), userForCheck.getName(), "The user names don't match");
        assertEquals(user.getLogin(), userForCheck.getLogin(), "The user logins don't match");
        assertEquals(user.getEmail(), userForCheck.getEmail(), "The user emails don't match");
        assertEquals(user.getBirthday(), userForCheck.getBirthday(), "The user birthdays don't match");
    }

    @Test
    public void createUserWithoutName() {
        User user = createUser();
        user.setName("");
        userController.createUser(user);

        assertEquals(1, userController.users.size(), "The number of users does not match the expected");

        User userForCheck = userController.users.get(1);

        assertEquals(user.getLogin(), userForCheck.getName(), "The user names don't match");
    }

    @Test
    public void createUserWithWrongLogin() {
        User user = createUser();
        user.setLogin("Test login");
        userController.createUser(user);

        assertEquals(0, userController.users.size(), "The number of users does not match the expected");
    }

    @Test
    public void createUserWithEmptyLogin() {
        User user = createUser();
        user.setLogin("");
        userController.createUser(user);

        assertEquals(0, userController.users.size(), "The number of users does not match the expected");
    }

    @Test
    public void createUserWithEmptyEmail() {
        User user = createUser();
        user.setEmail("");
        userController.createUser(user);

        assertEquals(0, userController.users.size(), "The number of users does not match the expected");
    }

    @Test
    public void createUserWithWrongEmail() {
        User user = createUser();
        user.setEmail("ya.ru");
        userController.createUser(user);

        assertEquals(0, userController.users.size(), "The number of users does not match the expected");
    }

    @Test
    public void createUserWithWrongBirthday() {
        User user = createUser();
        user.setBirthday(LocalDate.of(2026, 07, 12));
        userController.createUser(user);

        assertEquals(0, userController.users.size(), "The number of users does not match the expected");
    }

    @Test
    public void checkUpdateDate() {
        User user = createUser();
        userController.createUser(user);
        User updateUser = userController.users.get(1);
        updateUser.setName("Update_name");
        updateUser.setLogin("Update_login");
        updateUser.setEmail("update@ya.ru");
        userController.updateUser(updateUser);
        User userForCheck = userController.users.get(1);

        assertEquals(updateUser.getName(), userForCheck.getName(), "The user names don't match");
        assertEquals(updateUser.getLogin(), userForCheck.getLogin(), "The user logins don't match");
        assertEquals(updateUser.getEmail(), userForCheck.getEmail(), "The user emails don't match");
        assertEquals(updateUser.getBirthday(), userForCheck.getBirthday(), "The user birthdays don't match");
    }

    @Test
    public void getAllUsersTest() {
        User user = createUser();
        userController.createUser(user);
        User user2 = createUser();
        userController.createUser(user2);

        assertEquals(2, userController.getAllUsers().size(), "The number of users does not match the expected");
    }
}