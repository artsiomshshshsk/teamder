package com.github.artsiomshshshsk.findproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {
    enum Status {
        PENDING, ACCEPTED, REJECTED
    }
    @Id
    private Long id;
    @OneToOne
    private User applicant;
    @ManyToOne
    private Project project;
    @OneToOne
    private Role role;
    private Status status;

}
