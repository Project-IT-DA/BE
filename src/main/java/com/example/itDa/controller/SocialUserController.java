package com.example.itDa.controller;

import com.example.itDa.dto.response.LoginDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.service.user.GoogleUserService;
import com.example.itDa.service.user.KakaoUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SocialUserController {

    private final GoogleUserService googleUserService;
    private final KakaoUserService kakaoUserService;
    @ApiOperation(value = "카카오 로그인")
    @GetMapping("/users/login/kakao")
    public ResponseDto<LoginDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        return kakaoUserService.kakaoLogin(code, response);
    }
    @ApiOperation(value = "구글 로그인")
    @GetMapping("/users/login/google")
    public ResponseDto<LoginDto> googleLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        return googleUserService.googleLogin(code, response);
    }
}
