package com.example.itDa.domain.article.controller;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.article.response.ViewAllArticleResponseDto;
import com.example.itDa.domain.article.service.ArticleService;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Api(tags = "ARTICLE")
@RestController
public class ArticleController {
    private final ArticleService articleService;

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @ApiOperation(value = "거래글 작성")
    @PostMapping(value = "api/articles",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<ArticleResponseDto> registerArticle(@RequestPart(value = "data") ArticleRequestDto requestDto,
                                                           @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.registerArticle(requestDto, multipartFiles, userDetails);

    }

    @ApiOperation(value = "거래글 전체 조회")
    @GetMapping("api/articles")
    public ResponseDto<?> viewAllArticle(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.viewAllArticle(userDetails);
    }
    @ApiOperation(value = "거래글 단일 조회")
    @GetMapping("api/articles/{articleId}")
    public ResponseDto<?> viewArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.viewArticle(articleId, userDetails);
    }
    @ApiOperation(value = "거래글 수정")
    @PatchMapping("api/articles/{articleId}")
    public ResponseDto<?> editArticle(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long articleId,
                                      @RequestPart(value = "data") EditArticleRequestDto editRequestDto,
                                      @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) {
        return articleService.editArticle(userDetails, articleId, editRequestDto, multipartFiles);

    }
    @ApiOperation(value = "거래글 삭제")
    @DeleteMapping("api/articles/{articleId}")
    public ResponseDto<?> deleteArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return articleService.deleteArticle(userDetails, articleId);
    }
    @ApiOperation(value = "거래글 찜 하기/취소")
    @PostMapping("api/articles/{articleId}/like")
    public ResponseDto<?> likeArticle(@PathVariable Long articleId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(articleService.likeArticle(articleId, userDetails));
    }
    @ApiOperation(value = "거래글 검색", notes = "제목으로 검색 가능합니다,")
    @GetMapping("api/articles/search")
    public ResponseDto<List<ViewAllArticleResponseDto>> searchArticle(@RequestParam String title, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(articleService.searchArticle(title, userDetails));
    }
    @ApiOperation(value = "거래글 전체 조회 무한 스크롤")
    @GetMapping("api/articles2")
    public ResponseDto<Page<ViewAllArticleResponseDto>> getViewAllArticle(@PageableDefault(size = 5) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(articleRepository.getViewAllArticle(pageable, userDetails));
    }
    @ApiOperation(value = "거래 완료 체크")
    @PostMapping("api/articles/{articleId}/sell")
    public ResponseDto<?> soldOutArticle(@PathVariable Long articleId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseDto.success(articleService.soldOutArticle(articleId,userDetails));
    }

}
