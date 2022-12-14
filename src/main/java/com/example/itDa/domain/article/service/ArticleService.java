package com.example.itDa.domain.article.service;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.ArticleFile;
import com.example.itDa.domain.article.Like;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.repository.LikeRepository;
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
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private final S3UploaderService s3UploaderService;

    private final ArticleFileRepository articleFileRepository;

    private final LikeRepository likeRepository;


    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository,
                          S3UploaderService s3UploaderService, ArticleFileRepository articleFileRepository,
                          LikeRepository likeRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.s3UploaderService = s3UploaderService;
        this.articleFileRepository = articleFileRepository;
        this.likeRepository = likeRepository;
    }

    //????????? ??????
    @Transactional
    public ResponseDto<ArticleResponseDto> registerArticle(ArticleRequestDto requestDto,
                                                           MultipartFile[] multipartFiles,
                                                           UserDetailsImpl userDetails) {

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );

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


        List<String> fileUrls = new ArrayList<>();
        try {
            fileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "article");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> fileNames = new ArrayList<>();

        List<ArticleFile> articleFiles = new ArrayList<>();
        if (fileUrls != null) {
            for (int i = 0; i < fileUrls.size(); i++) {

                fileNames.add(multipartFiles[i].getOriginalFilename());
                articleFiles.add(ArticleFile.builder()
                        .article(article)
                        .fileUrl(fileUrls.get(i))
                        .fileName(fileNames.get(i))
                        .build());
            }
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

    // ????????? ?????? ??????
    public ResponseDto<?> viewAllArticle(UserDetailsImpl userDetails) {
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
                    .userId(article.getUser().getId())
                    .username(article.getUser().getUsername())
                    .articleId(article.getId())
                    .articleName(article.getArticleName())
                    .sellPrice(article.getSellPrice())
                    .category(article.getCategory())
                    .status(article.getStatus())
                    .location(article.getLocation())
                    .fileName(fileNames)
                    .fileUrl(fileUrls)
                    .createdAt(article.getCreatedAt())
                    .updatedAt(article.getUpdatedAt())
                    .like(likeCheck(article, userDetails.getUser()))
                    .build();
            responses.add(viewAllArticleResponseDto);
        }


        return ResponseDto.success(responses);
    }


    // ????????? ?????? ??????
    public ResponseDto<ArticleResponseDto> viewArticle(Long articleId, UserDetailsImpl userDetails) {

        Article article = getArticle(articleId);


        List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(articleId);
        List<String> fileNames = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();
        for (int i = 0; i < articleFiles.size(); i++) {
            fileNames.add(articleFiles.get(i).getFileName());
            fileUrls.add(articleFiles.get(i).getFileUrl());
        }

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .userId(article.getUser().getId())
                .username(article.getUser().getUsername())
                .density(article.getUser().getDensity())
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
                .like(likeCheck(article, userDetails.getUser()))
                .build();

        return ResponseDto.success(articleResponseDto);
    }

    // ????????? ??????
    @Transactional
    public ResponseDto<?> editArticle(UserDetailsImpl userDetails, Long articleId,
                                      EditArticleRequestDto editRequestDto,
                                      MultipartFile[] multipartFiles) {

        Article article = getArticle(articleId);
        // ?????? ?????? ?????? ??????
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400)
        );
        if (!user.equals(article.getUser())) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }
        // PUT ???????????? ????????? ????????? ??????, Url ??? ?????? ??? ?????? ??????
        List<ArticleFile> editFiles = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();

        // PUT ????????? ????????? ???????????? ?????? ????????? ????????? ????????? ?????? ?????? ????????? ?????? ?????? ????????? Url ??? ?????? ??? ?????? ??????
        List<ArticleFile> oldArticleFiles = articleFileRepository.findAllByArticleId(articleId);
        List<String> oldFileUrlsDB = new ArrayList<>();

        // ?????? ArticleFile ????????? oldArticleFiles ???????????? oldArticleFile ?????? Url ??? ?????? ????????? oldFileUrls ??? ?????????.
        for (ArticleFile oldArticleFile : oldArticleFiles) {
            oldFileUrlsDB.add(oldArticleFile.getFileUrl());
        }
        // PUT ??????????????? ????????? ?????? db??? ?????? fileUrls ???????????? ???????????? ?????? Url ??? ????????? db ?????? ??????, ????????? Url ??? DTO ??? ????????? ?????? ??? ?????? ??????
        List<String> deleteFileUrls = new ArrayList<>();
        // ?????? ?????? ??? ?????? ????????? ????????? null ????????? if ?????? ????????? null ??? ???????????? for ?????? ??????
        if (editRequestDto.getOldFileUrls() != null) {
            // ?????? ????????? ???????????? DB??? ?????? ????????? ???????????? URL ??? ?????????.???????????? ?????? Name ??? url ?????? ????????? ?????????.
            for (int i = 0; i < oldFileUrlsDB.size(); i++) {
                if (!editRequestDto.getOldFileUrls().contains(oldFileUrlsDB.get(i))) {

                    deleteFileUrls.add(oldFileUrlsDB.get(i));
                } else {
                    fileNames.add(oldArticleFiles.get(i).getFileName());
                    fileUrls.add(oldArticleFiles.get(i).getFileUrl());
                }
            }
        }
        // ????????? ????????? ?????? ?????? ???????????? ??????
        // DB ????????? ??????
        for (int i = 0; i < deleteFileUrls.size(); i++) {
            s3UploaderService.deleteFiles(deleteFileUrls.get(i), "article");
            articleFileRepository.deleteByFileUrl(deleteFileUrls.get(i));
        }
        // S3
        List<String> editFileUrls;
        try {
            editFileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "article");
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
                .status(article.getStatus())
                .build();
        return ResponseDto.success(editArticleResponseDto);
    }

    @Transactional
    public ResponseDto<?> deleteArticle(UserDetailsImpl userDetails, Long articleId) {
        // ?????? ?????? ??????
        Article article = getArticle(articleId);
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );
        if (!user.equals(article.getUser())) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }
        // ArticleId??? ????????? article??? ????????? ?????????.
        List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(articleId);
        // ???????????? ????????? Url ????????? ?????? ??? ?????? ????????? ??????????????? ????????????.
        List<String> articleFileUrls = new ArrayList<>();
        for (ArticleFile articleFile : articleFiles) {
            articleFileUrls.add(articleFile.getFileUrl());
        }
        // ???????????? ?????? Url ??? ???????????????.
        for (int i = 0; i < articleFileUrls.size(); i++) {
            s3UploaderService.deleteFiles(articleFileUrls.get(i), "article");
        }
        // articleFileDb ????????? ??????
        // articleDB ????????? article ??????
        articleFileRepository.deleteAll(articleFiles);
        articleRepository.delete(article);
        return ResponseDto.success("?????? ??????");
    }


    Article getArticle(Long articleId) {

        return articleRepository.findById(articleId).orElseThrow((
        ) -> new RequestException(ErrorCode.ARTICLE_NOT_FOUND_404));
    }

    User checkUser(Long userId){
        return userRepository.findById(userId).orElseThrow((
        )->new RequestException(ErrorCode.USER_NOT_EXIST));
    }

    public String likeArticle(Long articleId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Article article = getArticle(articleId);

        Optional<Like> existLike = likeRepository.findByUserAndArticle(user, article);

        if (!existLike.isPresent()) {
            Like like = new Like(article, user);
            likeRepository.save(like);

            return "??? ??????";
        } else {

            likeRepository.delete(existLike.get());

            return "??? ??????";
        }

    }

    public boolean likeCheck(Article article, User user) {
        return likeRepository.existsByUserAndArticle(user, article);
    }

    public List<ViewAllArticleResponseDto> searchArticle(String title, UserDetailsImpl userDetails) {

        User user = checkUser(userDetails.getUser().getId());

        List<Article> articleList = articleRepository.findByArticleNameContaining(title);
        List<ViewAllArticleResponseDto> responseDtoList = new ArrayList<>();

        if (articleList.isEmpty()) {
            throw new RequestException(ErrorCode.ARTICLE_NOT_SEARCH_404);
        }

            for (Article article : articleList) {
                List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(article.getId());
                List<String> fileNames = new ArrayList<>();
                List<String> fileUrls = new ArrayList<>();
                for (ArticleFile articleFile : articleFiles) {
                    fileNames.add(articleFile.getFileName());
                    fileUrls.add(articleFile.getFileUrl());
                }
                ViewAllArticleResponseDto viewAllArticleResponseDto = ViewAllArticleResponseDto.builder()
                        .userId(article.getUser().getId())
                        .username(article.getUser().getUsername())
                        .articleId(article.getId())
                        .articleName(article.getArticleName())
                        .sellPrice(article.getSellPrice())
                        .category(article.getCategory())
                        .status(article.getStatus())
                        .location(article.getLocation())
                        .fileName(fileNames)
                        .fileUrl(fileUrls)
                        .createdAt(article.getCreatedAt())
                        .updatedAt(article.getUpdatedAt())
                        .like(likeCheck(article, user))
                        .build();
                responseDtoList.add(viewAllArticleResponseDto);


            }
            return responseDtoList;
        }

    public ResponseDto<?> soldOutArticle(Long articleId, UserDetailsImpl userDetails) {
        Article article = getArticle(articleId);

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.NO_PERMISSION_TO_WRITE_NOTICE_400)
        );
        if (!user.equals(article.getUser())) {
            throw new RequestException(ErrorCode.NO_PERMISSION_TO_MODIFY_NOTICE_400);
        }
        if(article.getStatus()==Status.SELL){
            article.updateStatus(Status.SOLD_OUT);
            articleRepository.save(article);
            return ResponseDto.success("???????????? ????????? ?????? ???????????????.");
        }else{
            article.updateStatus(Status.SELL);
            articleRepository.save(article);
            return ResponseDto.success("???????????? ????????? ?????? ???????????????.");
        }

    }
}


