package com.example.reception.repository;

import com.example.reception.entity.ReceptionUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionUserRepository extends JpaRepository<ReceptionUser, Long> {

    boolean existsByUsername(String username);

    Optional<ReceptionUser> findByUsername(String username);
}