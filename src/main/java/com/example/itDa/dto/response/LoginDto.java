package com.example.itDa.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    // 프론트랑 통신
    private String email;
    private String profileImg;
}
