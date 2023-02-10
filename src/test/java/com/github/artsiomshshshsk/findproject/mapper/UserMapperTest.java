package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test

    void map() {
        //given
        User user = User.builder()
                .id(1L)
                .username("username")
                .build();

        //when
        UserResponse userResponse = userMapper.toUserResponse(user);
        //then
        assertEquals(userResponse.id(), user.getId());
        assertEquals(userResponse.username(), user.getUsername());
    }


}