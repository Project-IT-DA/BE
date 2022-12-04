package com.example.itDa.dto.response;

import com.example.itDa.domain.model.User;
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
    private String imgUrl;
    private List<CommentResponseDto> comments;

}
