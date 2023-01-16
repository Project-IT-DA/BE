package com.example.itDa.dto.response;

import com.example.itDa.domain.model.article.Category;
import com.example.itDa.domain.model.article.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String profileImg;
    private int density;
    private Long articleId;
    private String articleName;
    private String substance;
    private Category category;
    private Status status;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SS",timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SS",timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;
    private int sellPrice;
    private List<String> fileName;
    private List<String> fileUrl;
    private boolean like;


}
