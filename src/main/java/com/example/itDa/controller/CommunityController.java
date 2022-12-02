package com.example.itDa.controller;

import com.example.itDa.dto.request.CommunityRequestDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/community")
    public ResponseDto<?> createCommunity (@RequestBody CommunityRequestDto requestDto) {
        return communityService.createCommunity(requestDto);
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
    public ResponseDto<?> updateCommunity (@PathVariable("commuId") Long commuId, @RequestBody CommunityRequestDto requestDto){
        return communityService.updateCommunity(commuId, requestDto);
    }

    @DeleteMapping("/community/{commuId}")
    public ResponseDto<?> deleteCommunity (@PathVariable("commuId") Long commuId) {
        return communityService.deleteCommunity(commuId);
    }

}