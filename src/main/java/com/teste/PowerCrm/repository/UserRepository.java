package com.teste.PowerCrm.repository;

import com.teste.PowerCrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
