package com.github.artsiomshshshsk.findproject.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAll(Specification<Project> hasStatus, Pageable pageable);
    Page<Project> findAllByIsVisibleTrue(Pageable pageable);
}
