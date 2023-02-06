package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        List<User> allUsers = userController.getAllUsers();

        assertEquals(1, allUsers.size(),
                "The number of users does not match the expected.");

        User userForCheck = allUsers.get(0);

        assertEquals(user.getName(), userForCheck.getName(), "The user names don't match.");
        assertEquals(user.getLogin(), userForCheck.getLogin(), "The user logins don't match.");
        assertEquals(user.getEmail(), userForCheck.getEmail(), "The user emails don't match.");
        assertEquals(user.getBirthday(), userForCheck.getBirthday(),
                "The user birthdays don't match.");
    }

    @Test
    public void createUserWithoutName() {
        User user = createUser();
        user.setName("");
        userController.createUser(user);
        List<User> allUsers = userController.getAllUsers();

        assertEquals(1, allUsers.size(),
                "The number of users does not match the expected.");

        User userForCheck = allUsers.get(0);

        assertEquals(user.getLogin(), userForCheck.getName(), "The user names don't match.");
    }

    @Test
    public void createUserWithWrongLogin() {
        User user = createUser();
        user.setLogin("Test login");
        ValidationException e = assertThrows(ValidationException.class,
                () -> userController.createUser(user));
        assertEquals("An invalid login has been entered. The login cannot be empty and contain spaces.",
                e.getMessage());
        assertEquals(0, userController.getAllUsers().size(),
                "The number of users does not match the expected.");
    }

    @Test
    public void checkUpdateDate() {
        User user = createUser();
        userController.createUser(user);
        List<User> allUsers = userController.getAllUsers();
        User updateUser = allUsers.get(0);
        updateUser.setName("Update_name");
        updateUser.setLogin("Update_login");
        updateUser.setEmail("update@ya.ru");
        userController.updateUser(updateUser);
        List<User> allUsersAfterUpdate = userController.getAllUsers();
        User userForCheck = allUsersAfterUpdate.get(0);

        assertEquals(updateUser.getName(), userForCheck.getName(), "The user names don't match.");
        assertEquals(updateUser.getLogin(), userForCheck.getLogin(), "The user logins don't match.");
        assertEquals(updateUser.getEmail(), userForCheck.getEmail(), "The user emails don't match.");
        assertEquals(updateUser.getBirthday(), userForCheck.getBirthday(),
                "The user birthdays don't match.");
    }

    @Test
    public void checkUpdateWithWrongId() {
        User user = createUser();
        user.setId(100);
        ValidationException e = assertThrows(ValidationException.class,
                () -> userController.updateUser(user));
        assertEquals("The user with the specified id was not found.", e.getMessage());
        assertEquals(0, userController.getAllUsers().size(),
                "The number of users does not match the expected.");
    }

    @Test
    public void getAllUsersTest() {
        User user = createUser();
        userController.createUser(user);
        User user2 = createUser();
        userController.createUser(user2);

        assertEquals(2, userController.getAllUsers().size(),
                "The number of users does not match the expected.");
    }
}