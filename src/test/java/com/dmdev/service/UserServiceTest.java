package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;

    @Mock
    private UserDao userDao;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldLoginSuccessWithValidCredentials() {
        User user = getUser();
        UserDto userDto = getUserDto();
        doReturn(Optional.of(user))
                .when(userDao).findByEmailAndPassword(user.getEmail(), user.getPassword());
        doReturn(userDto)
                .when(userMapper).map(user);

        Optional<UserDto> actual = userService.login(user.getEmail(), user.getPassword());

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(userDto);
    }

    @Test
    void shouldLoginFailedWithInvalidCredentials() {
        doReturn(Optional.empty()).when(userDao).findByEmailAndPassword(any(), any());

        Optional<UserDto> actual = userService.login("DUMMY", "DUMMY");

        assertThat(actual).isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldCreateSuccess() {
        CreateUserDto createUserDto = getCreateUserDto();
        User user = getUser();
        UserDto userDto = getUserDto();

        doReturn(new ValidationResult()).when(createUserValidator).validate(createUserDto);
        doReturn(user).when(createUserMapper).map(createUserDto);
        doReturn(userDto).when(userMapper).map(user);

        UserDto actual = userService.create(createUserDto);

        assertThat(actual).isEqualTo(userDto);
        verify(userDao).save(user);
    }

    @Test
    void shouldThrowExceptionIfDtoInvalid() {
        CreateUserDto createUserDto = getCreateUserDto();
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of("test.error.code", "Test error code"));
        doReturn(validationResult).when(createUserValidator).validate(createUserDto);

        assertThrows(ValidationException.class, () -> userService.create(createUserDto));
        verifyNoInteractions(createUserMapper, userDao, userMapper);
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

    private static UserDto getUserDto() {
        return UserDto.builder()
                .id(1)
                .name("Ivan")
                .birthday(LocalDate.of(2000, 1, 1))
                .email("test@mail.com")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }

    private static User getUser() {
        return User.builder()
                .id(1)
                .name("Ivan")
                .birthday(LocalDate.of(2000, 1, 1))
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();
    }
}