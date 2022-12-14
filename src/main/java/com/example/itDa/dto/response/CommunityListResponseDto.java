package com.example.itDa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommunityListResponseDto {
    private Long commuId;
    private Long userId;
    private String username;
    private String createdAt;
    private String title;
    private String content;
    private List<String> imgUrls;
    private List<String> imgNames;
    private int commentsCnt;

}
