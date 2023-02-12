package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.domain.Role;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.dto.RoleRequest;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);

    default Project toProject(User user,ProjectRequest projectRequest){
        RoleRequest ownerRoleRequest = projectRequest.ownerRole();

        Role ownerRole = Role.builder()
                .name(ownerRoleRequest.name())
                .assignedUser(user)
                .build();


        List<Role> roles = new ArrayList<>();
        roles.add(ownerRole);

        projectRequest.roles().stream()
                .map(roleRequest -> Role.builder()
                        .name(roleRequest.name())
                        .build())
                .forEach(roles::add);

        return Project.builder()
                .name(projectRequest.name())
                .shortDescription(projectRequest.shortDescription())
                .description(projectRequest.description())
                .roles(roles)
                .owner(user)
                .build();
    }
}
