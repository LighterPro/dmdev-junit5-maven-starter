package ru.lighterpro.junit.service;

import org.junit.jupiter.api.*;
import ru.lighterpro.junit.dto.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Для примера можно сделать один на КЛАСС
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User OLGA = User.of(2, "Olga", "456");

    private UserService userService;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    // Tests block start //

    @Test
    void usersEmptyIfNoUsersAdded() {
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty(), "Users list should be empty");
    }

    @Test
    void userSizeIfUserAddedTest() {
        userService.add(IVAN);
        userService.add(OLGA);

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals(IVAN, user));
    }

    @Test
    void loginFailIfUsernameDoesNotExist() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login("WrongUsername", IVAN.getPassword());
        assertTrue(maybeUser.isEmpty());
    }

    @Test
    void loginFailIfPasswordIncorrect() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "WrongPassword");
        assertTrue(maybeUser.isEmpty());
    }

    // Tests block end //

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void afterAll() {
    }
}