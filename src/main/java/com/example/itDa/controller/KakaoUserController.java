package com.example.itDa.controller;

import com.example.itDa.dto.response.LoginDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.service.KakaoUserService;
import com.example.itDa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;
    private final UserService userService;

    @GetMapping("/users/kakao/callback")
    public ResponseDto<LoginDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        return kakaoUserService.kakaoLogin(code, response);
    }
}
