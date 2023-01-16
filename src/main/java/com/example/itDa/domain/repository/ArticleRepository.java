package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.article.Article;
import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    List<Article> findAllByOrderByCreatedAtDesc();

    List<Article> findByArticleNameContaining(String keyword);

    List<Article> findByUser(User user);

    Long countByUser(User user);


}
