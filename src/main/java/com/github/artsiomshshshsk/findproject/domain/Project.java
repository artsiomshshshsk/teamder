package com.github.artsiomshshshsk.findproject.domain;



import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(length = 1000)
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Role> roles;
    @ManyToOne
    private User owner;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "chat_invite_link")
    private String chatInviteLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Project project = (Project) o;
        return id != null && Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
