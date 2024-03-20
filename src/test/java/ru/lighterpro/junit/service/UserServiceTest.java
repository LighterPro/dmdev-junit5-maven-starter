package ru.lighterpro.junit.service;

import org.junit.jupiter.api.Test;
import ru.lighterpro.junit.dto.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserServiceTest {

    @Test
    void usersEmptyIfNoUsersAdded() {
        UserService userService = new UserService();
        List<User> allUsersList = userService.getAll();
        assertTrue(allUsersList.isEmpty(), "User list should be empty");
    }
}
