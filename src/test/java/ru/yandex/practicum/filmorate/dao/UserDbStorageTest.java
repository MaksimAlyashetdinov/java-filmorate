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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserStorage userStorage;

    @Test
    void createUserTest() {
        User testUser = createTestUser();
        userStorage.createUser(testUser);
        Optional<User> userFromMemory = Optional.ofNullable(userStorage.getUser(1));

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "test@yandex.ru")
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLogin")
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Maxim")
                );

        LocalDate testDate = LocalDate.of(1986, 07, 12);

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday", testDate)
                );
    }

    @Test
    void updateUserTest() {
        User testUser = createTestUser();
        userStorage.createUser(testUser);
        User userFromMemory = userStorage.getUser(1);
        userFromMemory.setLogin("Update_login");
        userFromMemory.setEmail("update@yandex.ru");
        userStorage.updateUser(userFromMemory);
        Optional<User> updateUser = Optional.ofNullable(userStorage.getUser(1));

        assertThat(updateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(updateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "update@yandex.ru")
                );

        assertThat(updateUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "Update_login")
                );
    }

    @Test
    void getAllUsersTest() {
        User user1 = createTestUser();
        userStorage.createUser(user1);
        User user2 = createTestUser();
        user2.setId(2);
        user2.setEmail("user2@yandex.ru");
        user2.setLogin("user2");
        userStorage.createUser(user2);
        Optional<List<User>> listOfUsersOptional = Optional.ofNullable(userStorage.getAllUsers());

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers).isNotNull()
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.size()).isEqualTo(2)
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("id", 2)
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("email",
                                "user2@yandex.ru")
                );

        assertThat(listOfUsersOptional)
                .isPresent()
                .hasValueSatisfying(listUsers ->
                        assertThat(listUsers.get(1)).hasFieldOrPropertyWithValue("login", "user2")
                );
    }

    @Test
    void getUserTest() {
        User testUser = createTestUser();
        userStorage.createUser(testUser);
        Optional<User> userFromMemory = Optional.ofNullable(userStorage.getUser(1));

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "test@yandex.ru")
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "testLogin")
                );

        assertThat(userFromMemory)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Maxim")
                );
    }

    private User createTestUser() {
        User user = new User(1, "test@yandex.ru", "testLogin", "Maxim",
                LocalDate.of(1986, 07, 12));
        return user;
    }
}