package com.example.itDa.domain.article.request;

import com.example.itDa.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ArticleRequestDto {
    private String articleName;
    private String substance;
    private String itemImg;
    private String location;
    private Category category;
    private int sellPrice;

    public ArticleRequestDto(String articleName, String substance, String itemImg, String location, Category category, int sellPrice) {
        this.articleName = articleName;
        this.substance = substance;
        this.itemImg = itemImg;
        this.location = location;
        this.category = category;
        this.sellPrice = sellPrice;
    }
}
