package com.github.artsiomshshshsk.findproject.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Project {
    @Id
    private Long id;
    private String name;
    private String shortDescription;
    private String description;
    @OneToMany
    private List<Role> roles;
    @ManyToOne
    private User owner;
    private LocalDateTime publishedAt;
    private ProjectStatus status;


}
