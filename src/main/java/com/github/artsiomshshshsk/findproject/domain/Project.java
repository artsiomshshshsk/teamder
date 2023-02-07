package com.github.artsiomshshshsk.findproject.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    private Long id;
    private String name;
    private String description;
    @OneToMany
    private List<Role> roles;
    @ManyToOne
    private User owner;
    private LocalDateTime createdAt;
}
