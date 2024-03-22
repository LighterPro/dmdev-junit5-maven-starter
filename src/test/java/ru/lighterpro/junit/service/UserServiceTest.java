package ru.lighterpro.junit.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import ru.lighterpro.junit.dto.User;
import ru.lighterpro.junit.extension.ConditionalExtension;
import ru.lighterpro.junit.extension.GlobalExtension;
import ru.lighterpro.junit.extension.ThrowableExtension;
import ru.lighterpro.junit.extension.UserServiceParamResolver;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.RepeatedTest.LONG_DISPLAY_NAME;

// Для примера можно сделать один на КЛАСС
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)

@Tag("fast")
@Tag("user")
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith(
        {UserServiceParamResolver.class, GlobalExtension.class, ConditionalExtension.class, ThrowableExtension.class}
)
public class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User OLGA = User.of(2, "Olga", "456");

    private UserService userService;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp(UserService userService) {
        this.userService = userService;
    }

    // 🟩 Tests block start 🟩 //

    @Test
    @DisplayName("The user list should be empty if no users have been added")
    void usersListEmptyIfNoUsersAdded() throws IOException {
        if (true) {
            throw new RuntimeException();
        }
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

    @RepeatedTest(value = 5, name = LONG_DISPLAY_NAME)
    @DisplayName("Transformation list of users into a Map<userId, User>>")
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
        @DisplayName("Throw an exception if username or password is null")
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

        @ParameterizedTest(name = "{arguments} test")
        @MethodSource("ru.lighterpro.junit.service.UserServiceTest#getArgumentsForLoginTest")
        void loginParametrizedTestWithMethodSource(String username, String password, Optional<User> user) {
            userService.add(IVAN, OLGA);
            Optional<User> maybeUser = userService.login(username, password);
            assertThat(maybeUser).isEqualTo(user);
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/login-test-data.csv", delimiter = ';', numLinesToSkip = 1)
        void loginParametrizedTestWithCSVFile(String username, String password) {
            userService.add(IVAN, OLGA);
            Optional<User> maybeUser = userService.login(username, password);
            assertThat(maybeUser).isNotEmpty();
        }

        @Test
        void checkLoginFunctionalityPerformance() {
            assertTimeout(
                    Duration.of(200L, ChronoUnit.MILLIS),
                    () -> {
//                        TimeUnit.MILLISECONDS.sleep(300); // не пройдет
                        TimeUnit.MILLISECONDS.sleep(100); // пройдет
                        return userService.login(IVAN.getUsername(), IVAN.getPassword());
                    }
            );
        }
    }

    static Stream<Arguments> getArgumentsForLoginTest() {
        return Stream.of(
                Arguments.of("Ivan", "123", Optional.of(IVAN)),
                Arguments.of("Olga", "456", Optional.of(OLGA)),
                Arguments.of("Ivan", "dummy", Optional.empty()),
                Arguments.of("dummy", "123", Optional.empty())
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