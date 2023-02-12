package com.github.artsiomshshshsk.findproject.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
        @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
        private Long id;
        private String name;
        @ManyToOne
        private User assignedUser;
}
