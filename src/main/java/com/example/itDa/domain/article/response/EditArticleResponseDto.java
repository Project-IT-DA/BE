package com.example.itDa.domain.article.response;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditArticleResponseDto {
    private Long id;
    private String articleName;
    private String substance;
    private String imgUrl;
    private Category category;
    private Status status;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int sellPrice;
}
