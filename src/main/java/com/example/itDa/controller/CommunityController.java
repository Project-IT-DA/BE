package com.example.itDa.controller;

import com.example.itDa.dto.request.CommunityRequestDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/community")
    public ResponseDto<?> createCommunity (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
                                           @RequestPart(value = "data") CommunityRequestDto requestDto) {
        return communityService.createCommunity(userDetails,multipartFiles,requestDto);
    }

    @GetMapping("/community")
    public ResponseDto<?> getAllCommunity () {
        return communityService.getAllCommunity();
    }

    @GetMapping("/community/{commuId}")
    public ResponseDto<?> getCommunity (@PathVariable("commuId") Long commuId){
        return communityService.getCommunity(commuId);
    }

    @PutMapping("/community/{commuId}")
    public ResponseDto<?> updateCommunity (@PathVariable("commuId") Long commuId,@AuthenticationPrincipal UserDetailsImpl userDetails ,@RequestBody CommunityRequestDto requestDto){
        return communityService.updateCommunity(commuId, userDetails, requestDto);
    }

    @DeleteMapping("/community/{commuId}")
    public ResponseDto<?> deleteCommunity (@PathVariable("commuId") Long commuId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return communityService.deleteCommunity(commuId, userDetails);
    }

}