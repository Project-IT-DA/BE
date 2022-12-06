package com.example.itDa.domain.article.service;

import com.example.itDa.domain.Status;
import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.ArticleFile;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.article.response.EditArticleResponseDto;
import com.example.itDa.domain.article.response.ViewAllArticleResponseDto;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.ArticleFileRepository;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.s3.S3UploaderService;
import com.example.itDa.infra.security.UserDetailsImpl;
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
    public ResponseDto<ArticleResponseDto> registerArticle(ArticleRequestDto requestDto,
                                                           MultipartFile[] multipartFiles,
                                                           UserDetailsImpl userDetails) {

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );
        System.out.println(userDetails.getUsername());

        Article article = Article.builder()
                .user(user)
                .articleName(requestDto.getArticleName())
                .sellPrice(requestDto.getSellPrice())
                .substance(requestDto.getSubstance())
                .location(requestDto.getLocation())
                .category(requestDto.getCategory())
                .build();

        articleRepository.save(article);


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
                    .article(article)
                    .fileUrl(fileUrls.get(i))
                    .fileName(fileNames.get(i))
                    .build());
        }
        articleFileRepository.saveAll(articleFiles);
        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .id(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .category(article.getCategory())
                .status(Status.SELL)
                .location(article.getLocation())
                .createdAt(article.getCreatedAt())
                .sellPrice(article.getSellPrice())
                .fileName(fileNames)
                .fileUrl(fileUrls)
                .build();
        return ResponseDto.success(articleResponseDto);
    }

    // 거래글 전체 조회
    public ResponseDto<?> viewAllArticle() {
        List<Article> articleList = articleRepository.findAllByOrderByCreatedAtDesc();
        List<ViewAllArticleResponseDto> responses = new ArrayList<>();

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            ViewAllArticleResponseDto viewAllArticleResponseDto = ViewAllArticleResponseDto.builder()
                    .id(article.getId())
                    .articleName(article.getArticleName())
                    .sellPrice(article.getSellPrice())
                    .category(article.getCategory())
                    .status(article.getStatus())
                    .location(article.getLocation())
//                    .fileName(fileNames)
//                    .fileUrl(fileUrls)
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .build();
            responses.add(viewAllArticleResponseDto);
        }
        return ResponseDto.success(responses);


    }

    // 거래글 단일 조회
    public ResponseDto<ArticleResponseDto> viewArticle(Long articleId) {
        Article article = getArticle(articleId);

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .id(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .category(article.getCategory())
                .status(article.getStatus())
                .location(article.getLocation())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .sellPrice(article.getSellPrice())

//                .fileName(fileNames)
//                .fileUrl(fileUrls)
                .build();

        return ResponseDto.success(articleResponseDto);
    }

    // 거래글 수정
    @Transactional
    public ResponseDto<?> editArticle(UserDetailsImpl userDetails, Long articleId, EditArticleRequestDto editRequestDto) {

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                ()-> new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400)
        );

        Article article = getArticle(articleId);

        if(!user.equals(article.getUser())){
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }

        article.update(editRequestDto);

        EditArticleResponseDto editArticleResponseDto = EditArticleResponseDto.builder()
                .articleName(editRequestDto.getArticleName())
                .substance(editRequestDto.getSubstance())
                .location(editRequestDto.getLocation())
                .sellPrice(editRequestDto.getSellPrice())
                .category(editRequestDto.getCategory())
                .build();
        return ResponseDto.success(editArticleResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteArticle(UserDetailsImpl userDetails, Long articleId) {

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                ()-> new RequestException(ErrorCode.USER_NOT_EXIST)
        );

        Article article = getArticle(articleId);

        if(!user.equals(article.getUser())){
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }

        articleRepository.delete(article);
        return ResponseDto.success("삭제 완료");
    }

    Article getArticle(Long articleId) {

        return articleRepository.findById(articleId).orElseThrow((
        ) -> new RequestException(ErrorCode.ARTICLE_NOT_FOUND_404));
    }


}

