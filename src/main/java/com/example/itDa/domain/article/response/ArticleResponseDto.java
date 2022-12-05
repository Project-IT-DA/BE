package com.example.itDa.domain.article.response;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.ArticleFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private Long id;
    private String articleName;
    private String substance;

    private Category category;
    private Status status;
    private String location;
    private LocalDateTime createdAt;
    private int sellPrice;

    private List<String> fileName;
    private List<String> fileUrl;

    public ArticleResponseDto(Article article) {
        this.id = article.getId();
        this.articleName = article.getArticleName();
        this.sellPrice = article.getSellPrice();
        this.category = article.getCategory();
        this.status = article.getStatus();
        this.location = article.getLocation();
    }


//    public static ArticleResponseDto from(Article article,List<ArticleFile> articleFiles) {
//        return ArticleResponseDto.builder()
//                .id(article.getId())
//                .articleName(article.getArticleName())
//                .substance(article.getSubstance())
//                .category(article.getCategory())
//                .status(article.getStatus())
//                .location(article.getLocation())
//                .sellPrice(article.getSellPrice())
//                .fileName(articleFiles.get)
//                .fileUrl(Collections.singletonList(articleFiles.getFileUrl()))
//                .build();
//    }

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
