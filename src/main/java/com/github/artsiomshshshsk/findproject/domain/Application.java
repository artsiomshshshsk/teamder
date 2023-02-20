package com.github.artsiomshshshsk.findproject.domain;

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
    private String applicationMessage;
    @ManyToOne
    private Project project;
    private String roleRequest;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;
}
