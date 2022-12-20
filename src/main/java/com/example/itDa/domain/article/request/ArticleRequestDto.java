package com.example.itDa.domain.article.request;

import com.example.itDa.domain.Category;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ArticleRequestDto {
    private String articleName;
    private String substance;
    private String location;
    private Category category;
    private int sellPrice;


}
