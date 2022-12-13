package com.example.itDa.domain.article.repository;

import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByCreatedAtDesc();

    List<Article> findByUser(User user);

    Long countByUser(User user);
}
