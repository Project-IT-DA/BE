package com.example.itDa.domain.article.controller;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.article.response.ViewAllArticleResponseDto;
import com.example.itDa.domain.article.service.ArticleService;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseDto<?> viewAllArticle(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.viewAllArticle(userDetails);
    }

    @GetMapping("api/articles/{articleId}")
    public ResponseDto<?> viewArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.viewArticle(articleId,userDetails);
    }

    @PutMapping("api/articles/{articleId}")
    public ResponseDto<?> editArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long articleId ,
                                      @RequestPart(value = "data") EditArticleRequestDto editRequestDto,
                                      @RequestPart(value = "file") MultipartFile[] multipartFiles){
        return articleService.editArticle(userDetails,articleId,editRequestDto,multipartFiles);

    }
    @DeleteMapping("api/articles/{articleId}")
    public ResponseDto<?> deleteArticle(@PathVariable Long articleId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return articleService.deleteArticle(userDetails,articleId);
    }

    @PostMapping("api/articles/{articleId}/like")
    public ResponseDto<?> likeArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseDto.success(articleService.likeArticle(articleId,userDetails));
    }

    @GetMapping("api/articles/search")
    public ResponseDto<List<ViewAllArticleResponseDto>> searchArticle(@RequestParam String title,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseDto.success(articleService.searchArticle(title,userDetails));
    }
}
