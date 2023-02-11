package com.github.artsiomshshshsk.findproject.repository;

import com.github.artsiomshshshsk.findproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
