package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceIT extends IntegrationTestBase {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = UserDao.getInstance();
        userService = new UserService(
                CreateUserValidator.getInstance(),
                userDao,
                CreateUserMapper.getInstance(),
                UserMapper.getInstance()
        );
    }

    @Test
    void shouldLoginWithValidCredentials() {
        User user = userDao.save(getUser("test1@gmail.com"));

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldCreateNewUser() {
        CreateUserDto createUserDto = getCreateUserDto();

        UserDto actualResult = userService.create(createUserDto);

        assertNotNull(actualResult.getId());
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

    private static CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01")
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN.toString())
                .gender(Gender.MALE.toString())
                .build();
    }
}
