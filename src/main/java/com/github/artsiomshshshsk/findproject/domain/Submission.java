package com.github.artsiomshshshsk.findproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
