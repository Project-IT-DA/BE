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
                                                           @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles){
        ArticleResponseDto articleResponseDto = articleService.registerArticle(requestDto, multipartFiles);
        return ResponseDto.success(articleResponseDto);
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
    public ResponseDto<EditArticleResponseDto> editArticle(@PathVariable Long articleId , @RequestBody EditArticleRequestDto editRequestDto){
        EditArticleResponseDto editArticleResponseDto = articleService.editArticle(articleId,editRequestDto);
        return ResponseDto.success(editArticleResponseDto);
    }
    @DeleteMapping("api/articles/{articleId}")
    public ResponseDto<String> deleteArticle(@PathVariable Long articleId){
        return ResponseDto.success(articleService.deleteArticle(articleId));
    }
}
