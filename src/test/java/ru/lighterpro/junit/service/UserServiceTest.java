package ru.lighterpro.junit.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.lighterpro.junit.dto.User;
import ru.lighterpro.junit.extension.UserServiceParamResolver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Для примера можно сделать один на КЛАСС
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)

@Tag("fast")
@Tag("user")
//@TestMethodOrder(MethodOrderer.MethodName.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        UserServiceParamResolver.class
)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User OLGA = User.of(2, "Olga", "456");

    private UserService userService;

    public UserServiceTest(TestInfo testInfo) {
        System.out.println();
    }

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp(UserService userService) {
        System.out.println("Before each");
        this.userService = userService;
    }

    // 🟩 Tests block start 🟩 //

    @Test
    @DisplayName("The user list should be empty if no users have been added")
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
    void usersListConvertedToMapById() {
        userService.add(IVAN, OLGA);

        Map<Integer, User> users = userService.getAllConvertedById();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), OLGA.getId()),
                () -> assertThat(users).containsValues(IVAN, OLGA)
        );
    }

    @Nested
    @Tag("login")
    @DisplayName("User login test")
    class LoginTests {
        @Test
        @DisplayName("")
        void throwExceptionIfUsernameOrPasswordIsNull() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> userService.login(null, "dummy"),
                            "login() should throw exception on null username"),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> userService.login("dummy", null),
                            "login() should throw exception on null password")
            );
        }

        @Test
        void loginFailIfPasswordIncorrect() {
            userService.add(IVAN);
            Optional<User> maybeUser = userService.login(IVAN.getUsername(), "WrongPassword");
            assertThat(maybeUser).isEmpty();
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
    }

    // 🟥 Tests block end 🟥 //

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void afterAll() {
    }
}