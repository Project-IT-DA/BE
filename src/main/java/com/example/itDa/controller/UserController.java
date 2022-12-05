package com.example.itDa.controller;

import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    // 마이페이지
    @GetMapping("/api/users/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return userService.getUserProfile(userDetails);
    }

    @DeleteMapping("/api/members/signout")
    public ResponseDto<Boolean> signOutMember(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return  userService.signOut(userDetails.getUser());

    }
}
