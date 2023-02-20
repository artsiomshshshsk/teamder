package com.github.artsiomshshshsk.findproject.domain;



import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @ToString.Exclude
    private List<Role> roles;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "project",
            fetch = FetchType.EAGER
    )
    private List<Application> applications;

    @ManyToOne
    private User owner;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "chat_invite_link")
    private String chatInviteLink;

    public boolean hasApplicant(User user){
        return applications.stream()
                .map(Application::getApplicant)
                .anyMatch(applicant->applicant.equals(user));
    }

    public boolean hasOpenedRole(String name){
        return roles.stream()
                .anyMatch(role -> role.getName().equals(name) && role.getAssignedUser() == null);
    }

    public void addApplication(Application application){
        applications.add(application);
    }
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
