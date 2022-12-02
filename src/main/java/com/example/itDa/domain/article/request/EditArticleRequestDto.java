package com.example.itDa.domain.article.request;

import com.example.itDa.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditArticleRequestDto {
    private String articleName;
    private String substance;
    private String itemImg;
    private String location;
    private int sellPrice;
    private Category category;
}
