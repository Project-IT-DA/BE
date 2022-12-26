package com.example.itDa.domain.article.repository;

import com.example.itDa.domain.article.response.ViewAllArticleResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    Page<ViewAllArticleResponseDto> getViewAllArticle(Pageable pageable, UserDetailsImpl userDetails);
}
