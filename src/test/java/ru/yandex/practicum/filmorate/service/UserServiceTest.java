package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

class UserServiceTest {

    private UserService userService;
    private UserStorage userStorage;

    @BeforeEach
    void start() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }
    @Test
    void addToFriends() {
        User user = createUser();
        userStorage.createUser(user);
        User user2 = createUser();
        userStorage.createUser(user2);

        assertEquals(0, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(0, user2.getFriends().size(),
                "The number of friends does not match the expected.");

        userService.addToFriends(user.getId(), user2.getId());

        assertEquals(1, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(1, user2.getFriends().size(),
                "The number of friends does not match the expected.");
    }

    @Test
    void deleteFromFriends() {
        User user = createUser();
        userStorage.createUser(user);
        User user2 = createUser();
        userStorage.createUser(user2);
        userService.addToFriends(user.getId(), user2.getId());

        assertEquals(1, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(1, user2.getFriends().size(),
                "The number of friends does not match the expected.");

        userService.deleteFromFriends(user.getId(), user2.getId());

        assertEquals(0, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(0, user2.getFriends().size(),
                "The number of friends does not match the expected.");
    }

    @Test
    void mutualFriends() {
        User user = createUser();
        userStorage.createUser(user);
        User user2 = createUser();
        userStorage.createUser(user2);
        User user3 = createUser();
        userStorage.createUser(user3);
        userService.addToFriends(user2.getId(), user3.getId());
        userService.addToFriends(user.getId(), user3.getId());

        List<User> mutualFriends = userService.mutualFriends(user.getId(), user2.getId());

        assertEquals(1, mutualFriends.size(),
                "The number of mutual friends does not match the expected.");
        assertEquals(user3, mutualFriends.get(0),
                "The ID of a mutual friend does not match");
    }

    @Test
    void getById() {
        User user = createUser();
        userService.createUser(user);

        assertEquals(1, userService.getUser(1).getId(), "Incorrect id");
    }

    @Test
    public void createUserWithFullInformation() {
        User user = createUser();
        userService.createUser(user);
        List<User> allUsers = userService.getAllUsers();

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
        userService.createUser(user);
        List<User> allUsers = userService.getAllUsers();

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
                () -> userService.createUser(user));
        assertEquals(
                "An invalid login has been entered. The login cannot be empty and contain spaces.",
                e.getMessage());
        assertEquals(0, userService.getAllUsers().size(),
                "The number of users does not match the expected.");
    }

    @Test
    public void checkUpdateDate() {
        User user = createUser();
        userService.createUser(user);
        List<User> allUsers = userService.getAllUsers();
        User updateUser = allUsers.get(0);
        updateUser.setName("Update_name");
        updateUser.setLogin("Update_login");
        updateUser.setEmail("update@ya.ru");
        userService.updateUser(updateUser);
        List<User> allUsersAfterUpdate = userService.getAllUsers();
        User userForCheck = allUsersAfterUpdate.get(0);

        assertEquals(updateUser.getName(), userForCheck.getName(), "The user names don't match.");
        assertEquals(updateUser.getLogin(), userForCheck.getLogin(),
                "The user logins don't match.");
        assertEquals(updateUser.getEmail(), userForCheck.getEmail(),
                "The user emails don't match.");
        assertEquals(updateUser.getBirthday(), userForCheck.getBirthday(),
                "The user birthdays don't match.");
    }

    @Test
    public void checkUpdateWithWrongId() {
        User user = createUser();
        user.setId(100);
        NotFoundException e = assertThrows(NotFoundException.class,
                () -> userService.updateUser(user));
        assertEquals("User" + user.getId() + "not found.", e.getMessage());
        assertEquals(0, userService.getAllUsers().size(),
                "The number of users does not match the expected.");
    }

    @Test
    public void getAllUsersTest() {
        User user = createUser();
        userService.createUser(user);
        User user2 = createUser();
        userService.createUser(user2);

        assertEquals(2, userService.getAllUsers().size(),
                "The number of users does not match the expected.");
    }

    private User createUser() {
        User user = new User();
        user.setName("Test_name");
        user.setLogin("Test_login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        return user;
    }
}