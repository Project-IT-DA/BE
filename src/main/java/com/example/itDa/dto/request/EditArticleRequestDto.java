package com.example.itDa.dto.request;

import com.example.itDa.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EditArticleRequestDto {
    private String articleName;
    private String substance;
    private String location;
    private int sellPrice;
    private List<String> oldFileUrls;
    private Category category;
}
