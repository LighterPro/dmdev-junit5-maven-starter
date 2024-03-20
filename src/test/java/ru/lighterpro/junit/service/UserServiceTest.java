package ru.lighterpro.junit.service;

import org.junit.jupiter.api.*;
import ru.lighterpro.junit.dto.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Для примера можно сделать один на КЛАСС
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    UserService userService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all");
        System.out.println();
    }

    @BeforeEach
    void setUp() {
        System.out.println("Before each: " + this);
        userService = new UserService();
    }

    @Test
    void usersEmptyIfNoUsersAdded() {
        System.out.println("Before tes1: " + this);
        List<User> users = userService.getAll();
        assertTrue(users.isEmpty(), "Users list should be empty");
    }

    @Test
    void userSizeIfUserAddedTest() {
        System.out.println("Before tes1: " + this);

        userService.add(new User());
        userService.add(new User());

        List<User> users = userService.getAll();
        assertEquals(2, users.size());
    }

    @AfterEach
    void tearDown() {
        System.out.println("After each: " + this);
        System.out.println();
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all");
        System.out.println();
    }
}