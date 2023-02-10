package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.Role;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.RoleResponse;
import com.github.artsiomshshshsk.findproject.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class RoleMapperTest {

    private RoleMapper roleMapper;

    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        roleMapper = Mappers.getMapper(RoleMapper.class);
    }

    @Test
    void toRoleResponse() {
        //given
        User user = User.builder()
                .id(1L)
                .username("username")
                .build();

        Role role = Role.builder()
                .id(1L)
                .name("name")
                .assignedUser(user)
                .isAvailable(true)
                .build();

        //when
        when(userMapper.toUserResponse(user)).thenReturn(UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build());

        RoleResponse roleResponse = roleMapper.toRoleResponse(role);
        //then
        assertEquals(roleResponse.id(), role.getId());
        assertEquals(roleResponse.name(), role.getName());
        assertEquals(roleResponse.assignedUser(), userMapper.toUserResponse(user));

    }
}