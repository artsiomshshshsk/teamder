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
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User applicant;
    private String resumeURL;
    private String message;
    @ManyToOne
    private Project project;
    @ManyToOne
    private Role roleRequest;
    private ApplicationStatus status;
    private LocalDateTime applicationDate;

}
