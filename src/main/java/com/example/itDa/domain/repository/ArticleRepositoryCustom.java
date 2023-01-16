package com.example.itDa.domain.repository;

import com.example.itDa.dto.response.ViewAllArticleResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<ViewAllArticleResponseDto> getViewAllArticle(Pageable pageable, UserDetailsImpl userDetails);
}
