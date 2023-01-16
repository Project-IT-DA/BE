package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.article.Article;
import com.example.itDa.domain.model.article.Like;
import com.example.itDa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like>  findByUserAndArticle(User user, Article article);
    Boolean existsByUserAndArticle(User user, Article article);


}
