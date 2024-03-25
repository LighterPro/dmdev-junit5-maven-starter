package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();

    @Test
    void shouldMapCreateUserDtoToUserCorrectly() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Ivan")
                .birthday("2000-01-01")
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN.toString())
                .gender(Gender.MALE.toString())
                .build();
        User actual = mapper.map(dto);
        User expected = User.builder()
                .name("Ivan")
                .birthday(LocalDate.of(2000, 01, 01))
                .email("test@mail.com")
                .password("1234567890")
                .role(Role.ADMIN)
                .gender(Gender.MALE)
                .build();

        assertThat(actual).isEqualTo(expected);
    }
}