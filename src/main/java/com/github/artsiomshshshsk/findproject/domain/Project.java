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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_seq")
    @SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq", allocationSize = 1)
    private Long id;
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(length = 1000)
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
    @ManyToOne
    private User owner;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;


}
