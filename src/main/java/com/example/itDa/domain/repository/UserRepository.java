package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
