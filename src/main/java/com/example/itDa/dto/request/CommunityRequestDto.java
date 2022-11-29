package com.example.itDa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRequestDto {
    private String title;
    private String content;
    private String imgUrl;
}
