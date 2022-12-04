package com.example.itDa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommunityListResponseDto {
    private Long commuId;
    private Long userId;
    private String title;
    private String content;
    private String imgUrl;
    private int commentsNum;

}
