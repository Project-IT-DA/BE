package com.example.itDa.domain.article.response;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
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
public class ViewAllArticleResponseDto {

    private Long articleId;
    private Long userId;
    private String username;
    private String articleName;
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
