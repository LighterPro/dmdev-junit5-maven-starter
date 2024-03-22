package ru.lighterpro.junit.service;

import ru.lighterpro.junit.dao.UserDao;
import ru.lighterpro.junit.dto.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class UserService {

    private final List<User> users = new ArrayList<>();
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean delete(Integer userId) {
        Integer userLocal = userId + 33; // Просто для примера
        return userDao.delete(userLocal);
    }

    public List<User> getAll() {
        return users;
    }

    public void add(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public Optional<User> login(String username, String password) {
        if (username == null) {
            throw new IllegalArgumentException("Username is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password is null");
        }

        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }

    public Map<Integer, User> getAllConvertedById() {
        return users.stream()
                .collect(Collectors.toMap(User::getId, identity()));
    }
}
