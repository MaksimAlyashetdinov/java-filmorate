package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private User createUser() {
        User user = new User();
        user.setName("Test_name");
        user.setLogin("Test_login");
        user.setEmail("ya@ya.ru");
        user.setBirthday(LocalDate.of(1986, 07, 12));
        return user;
    }
}