package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidationWithAllValidFields() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01")
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN.toString())
                .gender(Gender.MALE.toString())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertFalse(actual.hasErrors());
    }

    @Test
    void shouldFailValidationWithInvalidBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01 12:22:00") // wrong value
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN.toString())
                .gender(Gender.MALE.toString())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.getErrors()).hasSize(1);
        assertThat(actual.getErrors().getFirst().getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void shouldFailValidationWithInvalidRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01")
                .email("test@mail.com")
                .password("1234567890")
                .role("DUMMY") // wrong value
                .gender(Gender.MALE.toString())
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.getErrors()).hasSize(1);
        assertThat(actual.getErrors().getFirst().getCode()).isEqualTo("invalid.role");
    }

    @Test
    void shouldFailValidationWithInvalidGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01")
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN.toString())
                .gender("DUMMY") // wrong value
                .build();

        ValidationResult actual = validator.validate(dto);

        assertThat(actual.getErrors()).hasSize(1);
        assertThat(actual.getErrors().getFirst().getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void shouldFailValidationWithInvalidBirthdayRoleGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01 12:22:00") // wrong value
                .email("test@mail.com")
                .password("1234567890")
                .role("DUMMY") // wrong value
                .gender("DUMMY") // wrong value
                .build();

        ValidationResult actual = validator.validate(dto);
        List<String> errorCodes = actual.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(actual.getErrors()).hasSize(3);
        assertThat(errorCodes).contains("invalid.birthday", "invalid.role", "invalid.gender");
    }
}