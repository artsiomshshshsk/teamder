package com.github.artsiomshshshsk.findproject.project;



import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.user.User;
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
    @Column(name = "short_description",length = 300)
    private String shortDescription;

    @Column(length = 1000)
    private String description;
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Role> roles;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "project",
            fetch = FetchType.EAGER
    )
    private List<Application> applications;

    @ManyToOne
    @ToString.Exclude
    private User owner;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "chat_invite_link")
    private String chatInviteLink;

    @Column(name = "is_visible")
    private Boolean isVisible;

    public boolean hasApplicant(User user){
        return applications.stream()
                .map(Application::getApplicant)
                .anyMatch(applicant->applicant.equals(user));
    }

    public Optional<Role> findRoleByName(String roleName){
        return roles.stream()
                .filter(role -> role.getName().equals(roleName))
                .findFirst();
    }

    public Boolean hasOpenedRoleWithName(String roleName){
        return roles.stream()
                .filter(role -> role.getAssignedUser() == null)
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public Optional<Role> findOpenedRoleByName(String roleName){
        return roles.stream()
                .filter(role -> role.getAssignedUser() == null)
                .filter(role -> role.getName().equals(roleName))
                .findFirst();
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
