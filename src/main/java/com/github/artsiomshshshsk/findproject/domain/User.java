package com.github.artsiomshshshsk.findproject.domain;


import com.github.artsiomshshshsk.findproject.security.Role;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity(name = "_users")
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private String resumeURL;

    private String profilePictureURL;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner",
            fetch = FetchType.EAGER
    )
    private List<Project> projects;

    @OneToMany(
            mappedBy = "applicant",
            fetch = FetchType.EAGER
    )
    @ToString.Exclude
    private List<Application> applications;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void addApplication(Application application){
        applications.add(application);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
