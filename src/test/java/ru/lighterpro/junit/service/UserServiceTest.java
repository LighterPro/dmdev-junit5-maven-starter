package ru.lighterpro.junit.service;

import org.junit.jupiter.api.*;
import ru.lighterpro.junit.dto.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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

    // 🟩 Tests block start 🟩 //

    @Test
    void usersListEmptyIfNoUsersAdded() {
        List<User> users = userService.getAll();
        assertThat(users).as("Users list should be empty")
                .isEmpty();
    }

    @Test
    void userListSizeIfUserAdded() {
        userService.add(IVAN);
        userService.add(OLGA);

        List<User> users = userService.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), IVAN.getPassword());

        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(IVAN));
    }

    @Test
    void loginFailIfUsernameDoesNotExist() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login("WrongUsername", IVAN.getPassword());
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void loginFailIfPasswordIncorrect() {
        userService.add(IVAN);
        Optional<User> maybeUser = userService.login(IVAN.getUsername(), "WrongPassword");
        assertThat(maybeUser).isEmpty();
    }

    @Test
    void usersListConvertedToMapById() {
        userService.add(IVAN, OLGA);

        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), OLGA.getId()),
                () -> assertThat(users).containsValues(IVAN, OLGA)
        );
    }

    // 🟥 Tests block end 🟥 //

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void afterAll() {
    }
}