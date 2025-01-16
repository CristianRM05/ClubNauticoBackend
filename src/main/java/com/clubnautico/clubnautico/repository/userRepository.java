package com.clubnautico.clubnautico.repository;

import com.clubnautico.clubnautico.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
