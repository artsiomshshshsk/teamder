package com.github.artsiomshshshsk.findproject.user;


import com.github.artsiomshshshsk.findproject.application.Application;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.security.Role;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    private String contact;

    private String bio;


    private String resumeURL;

    private String profilePictureURL;

    private String verificationToken;

    private Boolean isVerified;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "owner",
            fetch = FetchType.EAGER
    )
    @ToString.Exclude
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

//    @Override
//    public boolean isEnabled() {
//        return isVerified;
//    }


    public boolean cvIsUsed(String cvUrl){
        if(resumeURL.equals(cvUrl)){
            return true;
        }
        for(Application application : applications){
            if(application.getResumeURL().equals(cvUrl)){
                return true;
            }
        }
        return false;
    }

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
