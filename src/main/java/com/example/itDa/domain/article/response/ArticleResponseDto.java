package com.example.itDa.domain.article.response;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
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
}
