package com.example.itDa.domain.article.repository;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByOrderByCreatedAtDesc();


    List<Article> findByArticleNameContaining(String keyword);

    List<Article> findByUser(User user);

    Long countByUser(User user);
}
