package com.example.itDa.dto.response;

import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.model.Community;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDto {
    private String email;
    private String nickname;
    private String createdAt;
    private String profileImge;
    private List<Article> articles;
    private List<Community> communities;


    public static ProfileResponseDto of(UserDetailsImpl userDetails) {
        return ProfileResponseDto.builder()
                .email(userDetails.getUser().getEmail())
                .nickname(userDetails.getUser().getNickname())
                .createdAt(userDetails.getUser().getCreatedAt())
                .profileImge(userDetails.getUser().getProfileImg())
                .build();
    }

}
