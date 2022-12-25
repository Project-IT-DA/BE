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
    private Long userId;
    private String username;
    private int density;
    private Long articleId;
    private String articleName;
    private String substance;
    private Category category;
    private Status status;
    private String location;
    private String createdAt;
    private String updatedAt;
    private int sellPrice;
    private List<String> fileName;
    private List<String> fileUrl;
    private boolean like;


}
