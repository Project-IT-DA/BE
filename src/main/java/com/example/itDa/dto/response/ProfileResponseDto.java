package com.example.itDa.dto.response;

import com.example.itDa.domain.model.article.Article;
import com.example.itDa.domain.repository.ArticleRepository;
import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.repository.CommunityRepository;
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
    private String username;
    private String profileImg;
    private List<Article> articles;
    private List<Community> communities;
    private Long density;


    public static ProfileResponseDto of(UserDetailsImpl userDetails, ArticleRepository articleRepository, CommunityRepository communityRepository) {
        return ProfileResponseDto.builder()
                .email(userDetails.getUser().getEmail())
                .username(userDetails.getUser().getUsername())
                .profileImg(userDetails.getUser().getProfileImg())
                .articles(articleRepository.findByUser(userDetails.getUser()))
                .communities(communityRepository.findByUser(userDetails.getUser()))
                .density(articleRepository.countByUser(userDetails.getUser()) + communityRepository.countByUser(userDetails.getUser()))
                .build();
    }

}
