package ru.yandex.practicum.filmorate.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

class UserServiceTest {

    UserService userService;
    InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void start() {
        userService = new UserService();
        inMemoryUserStorage = new InMemoryUserStorage();
    }
    @Test
    void addToFriends() {
        User user = createUser();
        inMemoryUserStorage.createUser(user);
        User user2 = createUser();
        inMemoryUserStorage.createUser(user2);

        assertEquals(0, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(0, user2.getFriends().size(),
                "The number of friends does not match the expected.");

        userService.addToFriends(user, user2);

        assertEquals(1, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(1, user2.getFriends().size(),
                "The number of friends does not match the expected.");
    }

    @Test
    void deleteFromFriends() {
        User user = createUser();
        inMemoryUserStorage.createUser(user);
        User user2 = createUser();
        inMemoryUserStorage.createUser(user2);
        userService.addToFriends(user, user2);

        assertEquals(1, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(1, user2.getFriends().size(),
                "The number of friends does not match the expected.");

        userService.deleteFromFriends(user, user2);

        assertEquals(0, user.getFriends().size(),
                "The number of friends does not match the expected.");
        assertEquals(0, user2.getFriends().size(),
                "The number of friends does not match the expected.");
    }

    @Test
    void mutualFriends() {
        User user = createUser();
        inMemoryUserStorage.createUser(user);
        User user2 = createUser();
        inMemoryUserStorage.createUser(user2);
        User user3 = createUser();
        inMemoryUserStorage.createUser(user3);
        userService.addToFriends(user2, user3);
        userService.addToFriends(user, user3);

        List<Integer> mutualFriends = userService.mutualFriends(user, user2);

        assertEquals(1, mutualFriends.size(),
                "The number of mutual friends does not match the expected.");
        assertEquals(3, mutualFriends.get(0),
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