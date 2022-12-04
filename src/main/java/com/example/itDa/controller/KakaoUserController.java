package com.example.itDa.controller;

import com.example.itDa.service.KakaoUserService;
import com.example.itDa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class KakaoUserController {

    private final KakaoUserService kakaoUserService;
    private final UserService userService;

    @GetMapping("/users/kakao/callback")
    public String kakaoLogin(@RequestParam String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

        return "redirect:/";
    }
}
