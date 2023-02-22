package com.github.artsiomshshshsk.findproject.user;

import com.github.artsiomshshshsk.findproject.user.dto.UserResponse;
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
    void givenUser_whenToUserResponse_thenReturnUserResponse() {
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

    @Test
    void givenUser_whenToUserResponse_thenReturnUserResponseWithNullId() {
        //given
        User user = User.builder()
                .username("username")
                .build();
        //when
        UserResponse userResponse = userMapper.toUserResponse(user);
        //then
        assertNull(userResponse.id());
        assertEquals(userResponse.username(), user.getUsername());
    }


}