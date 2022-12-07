package com.example.itDa.domain.article.controller;

import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.article.response.EditArticleResponseDto;
import com.example.itDa.domain.article.service.ArticleService;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.infra.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ArticleController {
    private final ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("api/articles")
    public ResponseDto<ArticleResponseDto> registerArticle(@RequestPart(value = "data")ArticleRequestDto requestDto,
                                                           @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.registerArticle(requestDto, multipartFiles, userDetails);

    }

    @GetMapping("api/articles")
    public ResponseDto<?> viewAllArticle(){
        return articleService.viewAllArticle();
    }

    @GetMapping("api/articles/{articleId}")
    public ResponseDto<?> viewArticle(@PathVariable Long articleId){
        return articleService.viewArticle(articleId);
    }

    @PutMapping("api/articles/{articleId}")
    public ResponseDto<?> editArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long articleId , @RequestBody EditArticleRequestDto editRequestDto){
        return articleService.editArticle(userDetails,articleId,editRequestDto);

    }
    @DeleteMapping("api/articles/{articleId}")
    public ResponseDto<?> deleteArticle(@PathVariable Long articleId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.deleteArticle(userDetails,articleId);
    }
}
