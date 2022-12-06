package com.example.itDa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleSocialDto {
        // 구글서버랑 통신
        private Long googleId; // email
        private String email;
        private String nickname;
        private String profileImg;
}
