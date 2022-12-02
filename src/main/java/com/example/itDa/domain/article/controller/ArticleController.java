package com.example.itDa.domain.article.controller;

import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.service.ArticleService;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.infra.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("api/articles")
    public ResponseDto<?> registerArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody ArticleRequestDto requestDto){
        return articleService.registerArticle(userDetails,requestDto);
    }

    @GetMapping("api/articles")
    public ResponseDto<?> viewAllArticle(){
        return articleService.viewAllArticle();
    }

    @GetMapping("api/articles/{articleId}")
    public ResponseDto<?> viewArticle(@PathVariable Long id){
        return articleService.viewArticle(id);
    }

    @PutMapping("api/articles/{articleId")
    public ResponseDto<?> editArticle(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long articleId , @RequestBody EditArticleRequestDto editRequestDto){
        return articleService.editArticle(userDetails,articleId,editRequestDto);
    }
    @DeleteMapping("api/articles/{articleId}")
    public ResponseDto<?> withdrawArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long articleId){
        return articleService.withdrawArticle(userDetails,articleId);
    }
}
