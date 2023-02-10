package com.github.artsiomshshshsk.findproject.service;


import com.github.artsiomshshshsk.findproject.domain.Project;
import com.github.artsiomshshshsk.findproject.domain.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectService {

    public Project createProject(User user){
        return Project.builder()
                .publishedAt(LocalDateTime.now())
                .owner(user)
                .build();
    }
}
