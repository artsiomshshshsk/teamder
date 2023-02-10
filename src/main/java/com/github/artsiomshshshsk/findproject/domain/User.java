package com.github.artsiomshshshsk.findproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity(name = "_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    private Long id;

    private String username;

    private String email;

    private String password;

    @OneToMany
    private List<Project> projects;
    @OneToMany
    private List<Submission> applications;

}
