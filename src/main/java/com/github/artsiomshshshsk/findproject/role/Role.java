package com.github.artsiomshshshsk.findproject.role;

import com.github.artsiomshshshsk.findproject.user.User;
import lombok.*;
import javax.persistence.*;


@Entity(name = "_roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        @ManyToOne
        private User assignedUser;
}
