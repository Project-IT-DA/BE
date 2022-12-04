package com.example.itDa.domain.article.service;

import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.ArticleFile;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.article.response.EditArticleResponseDto;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.ArticleFileRepository;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.s3.S3UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private final S3UploaderService s3UploaderService;

    private final ArticleFileRepository articleFileRepository;


    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository, S3UploaderService s3UploaderService, ArticleFileRepository articleFileRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.s3UploaderService = s3UploaderService;
        this.articleFileRepository = articleFileRepository;
    }

    //거래글 작성
    @Transactional
    public ArticleResponseDto registerArticle(ArticleRequestDto requestDto,
                                              MultipartFile[] multipartFiles) {
        Article registerArticle = articleRepository.save(
                Article.builder()
                        .articleName(requestDto.getArticleName())
                        .sellPrice(requestDto.getSellPrice())
                        .substance(requestDto.getSubstance())
                        .location(requestDto.getLocation())
                        .category(requestDto.getCategory())
                        .build()
        );
//        Article article = new Article(registerArticle);
//        article.updateStatus(Status.SELL);
        List<String> fileUrls;
        try {
            fileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "upload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> fileNames = new ArrayList<>();

        List<ArticleFile> articleFiles = new ArrayList<>();
        for (int i = 0; i < articleFiles.size(); i++) {

            fileNames.add(multipartFiles[i].getOriginalFilename());
            articleFiles.add(ArticleFile.builder()
                    .article(registerArticle)
                    .fileUrl(fileUrls.get(i))
                    .fileName(fileNames.get(i))
                    .build());
        }
        articleFileRepository.saveAll(articleFiles);
        return ArticleResponseDto.builder()
                .id(registerArticle.getId())
                .articleName(registerArticle.getArticleName())
                .substance(registerArticle.getSubstance())
                .category(registerArticle.getCategory())
                .status(registerArticle.getStatus())
                .location(registerArticle.getLocation())
                .createdAt(registerArticle.getCreatedAt())
                .sellPrice(registerArticle.getSellPrice())
                .fileName(fileNames)
                .fileUrl(fileUrls)
                .build();
    }

    public ResponseDto<?> viewAllArticle() {
        List<Article> articleList = articleRepository.findAllByOrderByCreatedAtDesc();
        List<ArticleResponseDto> responses = new ArrayList<>();

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(article);
            responses.add(articleResponseDto);
        }
        return ResponseDto.success(articleList);


    }

    public ResponseDto<?> viewArticle(Long articleId) {
        Article article = getArticle(articleId);
//                articleRepository.findById(id).orElseThrow((
//        ) -> new IllegalArgumentException("존재하지 않는 게시글 아이디 입니다."));
        return ResponseDto.success(article);
    }

    @Transactional
    public EditArticleResponseDto editArticle(Long articleId, EditArticleRequestDto editRequestDto) {
        Article article = getArticle(articleId);

        article.update(editRequestDto);

        return EditArticleResponseDto.builder()
                .articleName(editRequestDto.getArticleName())
                .substance(editRequestDto.getSubstance())
                .location(editRequestDto.getLocation())
                .sellPrice(editRequestDto.getSellPrice())
                .category(editRequestDto.getCategory())
                .build();
    }

    @Transactional
    public String deleteArticle(Long articleId) {
        Article article = getArticle(articleId);

        articleRepository.delete(article);
        return "삭제 완료";
    }

    Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow((
        ) -> new RequestException(ErrorCode.ARTICLE_NOT_FOUND_404));
//                new RuntimeException(ErrorCode.ARTICLE_NOT_FOUND_404));
    }

    User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RequestException(ErrorCode.UNAUTHORIZED_TOKEN));
    }

}
