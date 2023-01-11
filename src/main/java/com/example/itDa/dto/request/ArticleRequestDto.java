package com.example.itDa.dto.request;

import com.example.itDa.domain.Category;
import lombok.*;

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
