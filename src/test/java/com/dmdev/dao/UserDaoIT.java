package com.dmdev.dao;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.entity.Gender.FEMALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UserDaoIT extends IntegrationTestBase {

    private UserDao userDao = UserDao.getInstance();

    @Test
    void shouldSaveUser() {
        User user = getUser("test1@gmail.com");

        User actualResult = userDao.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void shouldFindAllUsers() {
        User user1 = userDao.save(getUser("test1@gmail.com"));
        User user2 = userDao.save(getUser("test2@gmail.com"));
        User user3 = userDao.save(getUser("test3@gmail.com"));

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> userIds = actualResult.stream().map(User::getId).toList();
        assertThat(userIds).contains(user1.getId(), user2.getId(), user3.getId());
    }

    @Test
    void shouldFindUserById() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void shouldNotFindUserByNonExistentId() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findById(Integer.MAX_VALUE);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void shouldFindUserByEmailAndPassword() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    @Test
    void shouldNotFindUserByIncorrectEmailAndPassword() {
        userDao.save(getUser("test1@gmail.com"));

        Optional<User> actualResult = userDao.findByEmailAndPassword("DUMMY", "DUMMY");

        assertThat(actualResult).isEmpty();
    }

    @Test
    void shouldDeleteExistingUser() {
        User user = userDao.save(getUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(user.getId());

        assertThat(actualResult).isTrue();
    }

    @Test
    void shouldNotDeleteNonExistentUser() {
        userDao.save(getUser("test1@gmail.com"));

        boolean actualResult = userDao.delete(Integer.MAX_VALUE);

        assertThat(actualResult).isFalse();
    }

    @Test
    void shouldUpdateExistingUser() {
        User user = getUser("test1@gmail.com");
        userDao.save(user);
        user.setName("Olga");
        user.setGender(FEMALE);

        userDao.update(user);
        Optional<User> actualResult = userDao.findById(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(user);
    }

    private static User getUser(String email) {
        return User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(2000, 1, 1))
                .email(email)
                .password("1234567890")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }
}