package com.github.artsiomshshshsk.findproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Submission {
    @Id
    private Long id;
    @OneToOne
    private User applicant;
    private String submissionMessage;
    @ManyToOne
    private Project project;
    @OneToOne
    private Role role;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime statusChangedAt;

}
