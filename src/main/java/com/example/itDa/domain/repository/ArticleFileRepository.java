package com.example.itDa.domain.repository;

import com.example.itDa.domain.article.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFile,Long> {
    List<ArticleFile> findAllByArticleId(Long id);

}
