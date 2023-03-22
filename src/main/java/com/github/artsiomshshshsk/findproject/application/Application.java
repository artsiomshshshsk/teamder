package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.role.Role;
import com.github.artsiomshshshsk.findproject.project.Project;
import com.github.artsiomshshshsk.findproject.user.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @ToString.Exclude
    private User applicant;
    private String resumeURL;
    private String message;
    @ManyToOne
    @ToString.Exclude
    private Project project;
    @ManyToOne(cascade = CascadeType.)
    @ToString.Exclude
    private Role roleRequest;
    private ApplicationStatus status;
    private LocalDateTime applicationDate;

    public User getProjectOwner(){
        return project.getOwner();
    }

}
