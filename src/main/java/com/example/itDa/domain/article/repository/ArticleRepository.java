package com.example.itDa.domain.article.repository;

import com.example.itDa.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
