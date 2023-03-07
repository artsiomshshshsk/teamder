package com.github.artsiomshshshsk.findproject.application;

import com.github.artsiomshshshsk.findproject.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByProject(Project project);
}
