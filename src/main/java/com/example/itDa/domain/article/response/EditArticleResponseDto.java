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
@AllArgsConstructor
@NoArgsConstructor
public class EditArticleResponseDto {
    private Long articleId;
    private String articleName;
    private String substance;
    private Category category;
    private Status status;
    private String location;
    private LocalDateTime updatedAt;
    private int sellPrice;
    private List<String> fileName;
    private List<String> fileUrl;
}
