package com.example.itDa.domain.article.controller;

import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.service.ArticleService;
import com.example.itDa.infra.global.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("api/articles")
    public ResponseDto<?> registerArticle(@RequestBody ArticleRequestDto requestDto){
        return articleService.registerArticle(requestDto);
    }

    @GetMapping("api/articles")
    public ResponseDto<?> viewAllArticle(){
        return articleService.viewAllArticle();
    }
    @GetMapping("api/articles/{articleId}")
    public ResponseDto<?> viewArticle(@PathVariable Long id){
        return articleService.viewArticle(id);
    }
}
