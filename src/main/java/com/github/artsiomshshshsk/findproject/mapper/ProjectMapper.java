package com.github.artsiomshshshsk.findproject.mapper;

import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.domain.Role;
import com.github.artsiomshshshsk.findproject.domain.User;
import com.github.artsiomshshshsk.findproject.dto.ProjectRequest;
import com.github.artsiomshshshsk.findproject.dto.ProjectResponse;
import com.github.artsiomshshshsk.findproject.dto.RoleRequest;
import com.github.artsiomshshshsk.findproject.dto.catalog.CatalogProjectResponse;
import org.mapstruct.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    default CatalogProjectResponse toCatalogProjectResponse(Project project){
        return CatalogProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .shortDescription(project.getShortDescription())
                .teamSize(project.getRoles().size())
                .occupiedPlaces((int) project.getRoles().stream()
                                .map(Role::getAssignedUser)
                                .filter(Objects::nonNull)
                                .count())
                .avatarURLs(
                        project.getRoles().stream()
                                .map(Role::getAssignedUser)
                                .filter(Objects::nonNull)
                                .map(User::getProfilePictureURL)
                                .toList()
                )
                .openedRoles(
                        project.getRoles().stream()
                                .filter(role -> role.getAssignedUser() == null)
                                .map(Role::getName)
                                .toList()
                )
                .build();
    }

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
                .chatInviteLink(projectRequest.chatInviteLink())
                .owner(user)
                .build();
    }
}
