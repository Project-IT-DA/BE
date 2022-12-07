package com.example.itDa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityResponseDto {

    private Long commuId;
    private Long userId;
    private String title;
    private String content;
    private List<CommentResponseDto> comments;
    private List<String> imgNames;
    private List<String> imgUrls;

}
