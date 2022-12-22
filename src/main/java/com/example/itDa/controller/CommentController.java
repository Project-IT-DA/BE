package com.example.itDa.controller;

import com.example.itDa.dto.request.CommentRequestDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.service.CommentService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;
    @ApiOperation(value = "댓글 작성")
    @PostMapping("/comment")
    public ResponseDto<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto){
        return commentService.createComment(userDetails, requestDto);
    }
    @ApiOperation(value = "댓글 수정")
    @PutMapping("/comment/{commentId}")
    public ResponseDto<?> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(userDetails,
                commentId,
                requestDto);
    }
    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/comment/{commentId}")
    public ResponseDto<?> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId){
        return commentService.deleteComment(userDetails, commentId);
    }

}

