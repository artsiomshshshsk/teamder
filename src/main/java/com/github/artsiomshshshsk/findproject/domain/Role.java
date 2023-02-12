package com.github.artsiomshshshsk.findproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity(name = "_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
        @Id
        private Long id;
        private String name;
        @ManyToOne
        private User assignedUser;
        private boolean isAvailable;
}
