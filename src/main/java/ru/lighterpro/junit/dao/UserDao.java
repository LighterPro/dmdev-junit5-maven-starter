package ru.lighterpro.junit.dao;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

public class UserDao {

    @SneakyThrows
    public boolean delete(Integer userId) {
        try (Connection connection = DriverManager.getConnection("url", "user", "password")) {
            // Представим, что тут написана логика удаления User из базы по userId
            return true;
        }
    }
}
