package com.example.itDa.controller;

import com.example.itDa.dto.request.UpdateProfileDto;
import com.example.itDa.dto.response.ProfileResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.service.user.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "USER")

public class UserController {
    private final UserService userService;

    // 마이페이지 조회, 수정
    @GetMapping("/api/users/profile")
    public ResponseDto<ProfileResponseDto> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserProfile(userDetails);
    }

    @PutMapping("/api/users/profile")
    public ResponseDto<ProfileResponseDto> updateUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                             @RequestPart(value = "file", required = false)MultipartFile multipartFile,
                                                             @RequestPart(value = "data")UpdateProfileDto updateProfileDto) {
        return userService.updateUserProfile(userDetails, multipartFile, updateProfileDto);
    }

    @DeleteMapping("/api/users/signout")
    public ResponseDto<String> signout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.signout(userDetails);
    }

}
