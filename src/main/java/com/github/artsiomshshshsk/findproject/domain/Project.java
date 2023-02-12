package com.github.artsiomshshshsk.findproject.domain;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    private Long id;
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(length = 1000)
    private String description;
    @OneToMany
    private List<Role> roles;
    @ManyToOne
    private User owner;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;


}
