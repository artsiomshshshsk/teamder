package com.github.artsiomshshshsk.findproject.role;

import com.github.artsiomshshshsk.findproject.role.dto.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
