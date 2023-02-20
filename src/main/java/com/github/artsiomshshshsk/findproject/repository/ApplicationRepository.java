package com.github.artsiomshshshsk.findproject.repository;

import com.github.artsiomshshshsk.findproject.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
