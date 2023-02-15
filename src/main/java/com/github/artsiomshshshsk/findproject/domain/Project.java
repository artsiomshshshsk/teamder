package com.github.artsiomshshshsk.findproject.domain;



import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(length = 1000)
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
    @ManyToOne
    private User owner;
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(name = "chat_invite_link")
    private String chatInviteLink;


}
