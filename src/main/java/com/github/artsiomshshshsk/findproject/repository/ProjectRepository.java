package com.github.artsiomshshshsk.findproject.repository;

import com.github.artsiomshshshsk.findproject.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
