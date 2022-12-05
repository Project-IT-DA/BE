package com.example.itDa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSocialDto {
// 카카오서버랑 통신
    private Long kakaoId; // email
    private String email;
    private String nickname;
    private String profileImg;
}
