package ru.yandex.practicum.filmorate.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.UsersFriendsStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UsersFriendsDbStorageTest {

    private final UsersFriendsStorage usersFriendsStorage;
    private final UserStorage userStorage;

    @Test
    void addToFriends() {
        User testUser1 = createTestUser();
        userStorage.createUser(testUser1);
        Optional<User> userFromMemory1 = Optional.ofNullable(userStorage.getUser(1));
        User testUser2 = createTestUser();
        testUser2.setId(2);
        testUser2.setLogin("user2");
        testUser2.setEmail("user2@yandex.ru");
        userStorage.createUser(testUser2);
        usersFriendsStorage.addToFriends(1, 2);
        Optional<List<User>> userId1Friends = Optional.ofNullable(
                usersFriendsStorage.getAllFriends(1));
        Optional<User> userId2 = Optional.ofNullable(userStorage.getUser(2));

        assertThat(userId1Friends)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.size()).isEqualTo(1)
                );

        assertThat(userId1Friends)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.contains(userId2.orElse(null))).isEqualTo(
                                true)
                );
    }

    @Test
    void deleteFromFriends() {
        User testUser1 = createTestUser();
        userStorage.createUser(testUser1);
        Optional<User> userFroMemory1 = Optional.ofNullable(userStorage.getUser(1));
        User testUser2 = createTestUser();
        testUser2.setId(2);
        testUser2.setLogin("user2");
        testUser2.setEmail("user2@yandex.ru");
        userStorage.createUser(testUser2);
        usersFriendsStorage.addToFriends(1, 2);
        usersFriendsStorage.deleteFromFriends(1, 2);
        Optional<List<User>> userId1FriendsWithoutFriend = Optional.ofNullable(
                usersFriendsStorage.getAllFriends(1));

        assertThat(userId1FriendsWithoutFriend)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.size()).isEqualTo(0)
                );
    }

    @Test
    void mutualFriends() {
        User testUser1 = createTestUser();
        userStorage.createUser(testUser1);
        User testUser2 = createTestUser();
        testUser2.setId(2);
        testUser2.setLogin("user2");
        testUser2.setEmail("user2@yandex.ru");
        userStorage.createUser(testUser2);
        User testUser3 = createTestUser();
        testUser3.setId(3);
        testUser3.setLogin("user3");
        testUser3.setEmail("user3@yandex.ru");
        userStorage.createUser(testUser3);
        usersFriendsStorage.addToFriends(1, 3);
        usersFriendsStorage.addToFriends(2, 3);
        Optional<List<User>> mutualFriends = Optional.ofNullable(
                usersFriendsStorage.mutualFriends(1, 2));
        Optional<User> user3 = Optional.ofNullable(userStorage.getUser(3));

        assertThat(mutualFriends)
                .isPresent()
                .hasValueSatisfying(userId1FriendsList ->
                        assertThat(userId1FriendsList.size()).isEqualTo(1)
                );

        assertThat(mutualFriends)
                .isPresent()
                .hasValueSatisfying(friends ->
                        assertThat(friends.contains(user3.orElse(null))).isEqualTo(true)
                );
    }

    private User createTestUser() {
        User user = new User(1, "test@yandex.ru", "testLogin", "Maxim",
                LocalDate.of(1986, 07, 12));
        return user;
    }
}