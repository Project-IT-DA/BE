package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.Comment;
import com.example.itDa.domain.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCommunity(Community community);

    int countAllByCommunity(Community community);
}