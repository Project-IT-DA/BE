package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findByUser(User user);

    Long countByUser(User user);
}
