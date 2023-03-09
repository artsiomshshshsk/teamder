package com.github.artsiomshshshsk.findproject.project;

import com.github.artsiomshshshsk.findproject.project.dto.*;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.user.User;
import com.github.artsiomshshshsk.findproject.role.dto.RoleRequest;
import org.mapstruct.Mapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);


    default ProjectProfileResponse toProjectProfileResponse(Project project){
        return ProjectProfileResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .shortDescription(project.getShortDescription())
                .description(project.getDescription())
                .openedRoles(
                        project.getRoles().stream()
                                .filter(role -> role.getAssignedUser() == null)
                                .map(role -> ProjectProfileOpenedRoleResponse.builder()
                                        .name(role.getName())
                                        .id(role.getId())
                                        .build()
                                )
                                .toList())
                .publishedAt(project.getPublishedAt())
                .teamSize(project.getRoles().size())
                .occupiedPlaces((int) project.getRoles().stream().filter(role -> role.getAssignedUser() != null).count())
                .teamMembers(
                        project.getRoles().stream()
                                .filter(role -> role.getAssignedUser() != null)
                                .map(role -> TeamMemberResponse.builder()
                                        .userId(role.getAssignedUser().getId())
                                        .username(role.getAssignedUser().getUsername())
                                        .roleName(role.getName())
                                        .build()
                                )
                                .toList()
                )
                .ownerId(project.getOwner().getId())
                .build();
    }

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
