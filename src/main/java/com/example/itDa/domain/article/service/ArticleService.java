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
                .status(Status.SELL)
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
        for (int i = 0; i < fileUrls.size(); i++) {

            fileNames.add(multipartFiles[i].getOriginalFilename());
            articleFiles.add(ArticleFile.builder()
                    .article(article)
                    .fileUrl(fileUrls.get(i))
                    .fileName(fileNames.get(i))
                    .build());
        }
        articleFileRepository.saveAll(articleFiles);
        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .articleId(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .category(article.getCategory())
                .status(article.getStatus())
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


        for (Article article : articleList) {
            List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(article.getId());
            List<String> fileNames = new ArrayList<>();
            List<String> fileUrls = new ArrayList<>();
            for (ArticleFile articleFile : articleFiles) {
                fileNames.add(articleFile.getFileName());
                fileUrls.add(articleFile.getFileUrl());
            }
            ViewAllArticleResponseDto viewAllArticleResponseDto = ViewAllArticleResponseDto.builder()
                    .id(article.getId())
                    .articleName(article.getArticleName())
                    .sellPrice(article.getSellPrice())
                    .category(article.getCategory())
                    .status(article.getStatus())
                    .location(article.getLocation())
                    .fileName(fileNames)
                    .fileUrl(fileUrls)
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


        List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(articleId);
        List<String> fileNames = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();
        for (int i = 0; i < articleFiles.size(); i++) {
            fileNames.add(articleFiles.get(i).getFileName());
            fileUrls.add(articleFiles.get(i).getFileUrl());
        }

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .articleId(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .category(article.getCategory())
                .status(article.getStatus())
                .location(article.getLocation())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .sellPrice(article.getSellPrice())
                .fileName(fileNames)
                .fileUrl(fileUrls)
                .build();

        return ResponseDto.success(articleResponseDto);
    }

    // 거래글 수정
    @Transactional
    public ResponseDto<?> editArticle(UserDetailsImpl userDetails, Long articleId,
                                      EditArticleRequestDto editRequestDto,
                                      MultipartFile[] multipartFiles) {

        Article article = getArticle(articleId);
        // 유저 수정 권한 검사
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400)
        );
        if (!user.equals(article.getUser())) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }
        // PUT 요청으로 들어올 파일과 이름, Url 을 담을 빈 배열 생성
        List<ArticleFile> editFiles = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();

        // PUT 요청이 들어온 거래글에 있는 파일과 기존의 파일의 변경 내용 비교를 위해 기존 파일의 Url 을 담을 빈 배열 생성
        List<ArticleFile> oldArticleFiles = articleFileRepository.findAllByArticleId(articleId);
        List<String> oldFileUrlsDB = new ArrayList<>();

        // 기존 ArticleFile 타입의 oldArticleFiles 배열에서 oldArticleFile 에서 Url 을 추출 리스트 oldFileUrls 에 담는다.
        for (ArticleFile oldArticleFile : oldArticleFiles) {
            oldFileUrlsDB.add(oldArticleFile.getFileUrl());
        }
        // PUT 요청으로온 파일과 기존 db에 있는 fileUrls 리스트를 비교하여 기존 Url 이 없으면 db 에서 삭제, 추가된 Url 은 DTO 에 추가를 위해 빈 배열 생성
        List<String> deleteFileUrls = new ArrayList<>();
        // 기존 파일 과 수정 파일의 차이가 null 이라면 if 문을 나가고 null 이 아니라면 for 문을 구동
        if (editRequestDto.getOldFileUrls() != null) {
            // 수정 요청온 파일들중 DB에 없는 파일은 삭제파일 URL 에 넣는다.아니라면 다시 Name 와 url 들을 배열에 담는다.
            for (int i = 0; i < oldFileUrlsDB.size(); i++) {
                if (!editRequestDto.getOldFileUrls().contains(oldFileUrlsDB.get(i))) {

                    deleteFileUrls.add(oldFileUrlsDB.get(i));
                } else {
                    fileNames.add(oldArticleFiles.get(i).getFileName());
                    fileUrls.add(oldArticleFiles.get(i).getFileUrl());
                }
            }
        }
        // 삭제할 파일이 있는 경우 버킷에서 삭제
        // DB 에서도 삭제
        for (int i = 0; i < deleteFileUrls.size(); i++) {
            s3UploaderService.deleteFiles(deleteFileUrls.get(i), "upload");
            articleFileRepository.deleteByFileUrl(deleteFileUrls.get(i));
        }
        // S3
        List<String> editFileUrls;
        try {
            editFileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "upload");
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        if (null != multipartFiles) {
            for (int i = 0; i < multipartFiles.length; i++) {
                fileNames.add(multipartFiles[i].getOriginalFilename());
                fileUrls.add(editFileUrls.get(i));

                editFiles.add(ArticleFile.builder()
                        .fileName(multipartFiles[i].getOriginalFilename())
                        .fileUrl(editFileUrls.get(i))
                        .article(article)
                        .build());
            }
        }
        articleFileRepository.saveAll(editFiles);

        article.update(editRequestDto);

        EditArticleResponseDto editArticleResponseDto = EditArticleResponseDto.builder()
                .articleId(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .location(article.getLocation())
                .sellPrice(article.getSellPrice())
                .category(article.getCategory())
                .fileName(fileNames)
                .fileUrl(fileUrls)
                .updatedAt(article.getUpdatedAt())
                .build();
        return ResponseDto.success(editArticleResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteArticle(UserDetailsImpl userDetails, Long articleId) {
        // 유저 권한 검사
        Article article = getArticle(articleId);
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );
        if (!user.equals(article.getUser())) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }
        // ArticleId로 삭제할 article의 파일을 찾는다.
        List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(articleId);
        // 버킷에서 삭제할 Url 추출을 위해 빈 배열 생성후 반복문으로 담아준다.
        List<String> articleFileUrls = new ArrayList<>();
        for (ArticleFile articleFile : articleFiles) {
            articleFileUrls.add(articleFile.getFileUrl());
        }
        // 빈배열에 담긴 Url 을 삭제해준다.
        for(int i = 0; i < articleFileUrls.size(); i++){
            s3UploaderService.deleteFiles(articleFileUrls.get(i),"upload");
        }
        // articleFileDb 에서도 삭제
        // articleDB 에서도 article 삭제
        articleFileRepository.deleteAll(articleFiles);
        articleRepository.delete(article);
        return ResponseDto.success("삭제 완료");
    }

    Article getArticle(Long articleId) {

        return articleRepository.findById(articleId).orElseThrow((
        ) -> new RequestException(ErrorCode.ARTICLE_NOT_FOUND_404));
    }


}

