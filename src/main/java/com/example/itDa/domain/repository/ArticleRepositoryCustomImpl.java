package com.example.itDa.domain.repository;

import com.example.itDa.domain.model.Article;
import com.example.itDa.domain.model.ArticleFile;
import com.example.itDa.dto.response.ViewAllArticleResponseDto;
import com.example.itDa.domain.model.User;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.example.itDa.domain.article.QArticle.article;


public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final LikeRepository likeRepository;
    private final JPAQueryFactory queryFactory;
    private final ArticleFileRepository articleFileRepository;

    public ArticleRepositoryCustomImpl(EntityManager em, LikeRepository likeRepository,
                                       ArticleFileRepository articleFileRepository) {
        this.likeRepository = likeRepository;
        this.queryFactory = new JPAQueryFactory(em);
        this.articleFileRepository = articleFileRepository;
    }

    @Override
    public Page<ViewAllArticleResponseDto> getViewAllArticle(Pageable pageable, UserDetailsImpl userDetails) {
        List<Article> result = queryFactory
                .selectFrom(article)
                .where(article.user.eq(userDetails.getUser()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.createdAt.desc())
                .fetch();

        List<Article> total = queryFactory
                .selectFrom(article)
                .where(article.user.eq(userDetails.getUser()))
                .orderBy(article.createdAt.desc())
                .fetch();

        List<ViewAllArticleResponseDto> articleResponseDtoList = new ArrayList<>();


        for (Article findArticle : result) {
            List<ArticleFile> articleFiles = articleFileRepository.findAllByArticleId(findArticle.getId());
            List<String> fileNames = new ArrayList<>();
            List<String> fileUrls = new ArrayList<>();
            for (ArticleFile articleFile : articleFiles) {
                fileNames.add(articleFile.getFileName());
                fileUrls.add(articleFile.getFileUrl());
            }
            articleResponseDtoList.add(
                    ViewAllArticleResponseDto.builder()
                            .userId(findArticle.getUser().getId())
                            .username(findArticle.getUser().getUsername())
                            .articleId(findArticle.getId())
                            .articleName(findArticle.getArticleName())
                            .sellPrice(findArticle.getSellPrice())
                            .category(findArticle.getCategory())
                            .status(findArticle.getStatus())
                            .location(findArticle.getLocation())
                            .fileName(fileNames)
                            .fileUrl(fileUrls)
                            .createdAt(findArticle.getCreatedAt())
                            .updatedAt(findArticle.getUpdatedAt())
                            .like(likeCheck(findArticle, userDetails.getUser()))
                            .build()
            );

        }

        return new PageImpl<>(articleResponseDtoList, pageable, total.size());
    }


    public boolean likeCheck(Article article, User user) {
        return likeRepository.existsByUserAndArticle(user, article);
    }
}

