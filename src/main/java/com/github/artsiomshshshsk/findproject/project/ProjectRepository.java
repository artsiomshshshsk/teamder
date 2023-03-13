package com.github.artsiomshshshsk.findproject.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.roles r WHERE p.name LIKE %:query% OR p.shortDescription LIKE %:query% OR r.name LIKE %:query%")
    Page<Project> search(Pageable pageable, @Param("query") String query);
    Page<Project> findAll(Specification<Project> hasStatus, Pageable pageable);
    Page<Project> findAllByIsVisibleTrue(Pageable pageable);
}
