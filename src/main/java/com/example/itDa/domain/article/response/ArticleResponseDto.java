package com.example.itDa.domain.article.response;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ArticleResponseDto {

    private Long id;
    private String articleName;
    private String substance;
    private String imgUrl;
    private Category category;
    private Status status;
    private String location;
    private LocalDateTime createdAt;
    private int sellPrice;


    public ArticleResponseDto(Article registerArticle) {
        this.id = registerArticle.getId();
        this.articleName = registerArticle.getSubstance();
        this.imgUrl = registerArticle.getItemImg();
        this.articleName = registerArticle.getArticleName();
        this.category = registerArticle.getCategory();
        this.status = registerArticle.getStatus();
        this.location = registerArticle.getLocation();
        this.createdAt = registerArticle.getCreatedAt();
        this.sellPrice = registerArticle.getSellPrice();
    }

    public static ArticleResponseDto from(Article article) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .articleName(article.getArticleName())
                .substance(article.getSubstance())
                .imgUrl(article.getItemImg())
                .category(article.getCategory())
                .status(article.getStatus())
                .location(article.getLocation())
                .sellPrice(article.getSellPrice())
                .build();

    }

    public static ArticleResponseDto viewAll(Article article) {
        return ArticleResponseDto.builder()
                .id(article.getId())
                .articleName(article.getArticleName())
                .sellPrice(article.getSellPrice())
                .category(article.getCategory())
                .status(article.getStatus())
                .location(article.getLocation())
                .build();
    }
}
