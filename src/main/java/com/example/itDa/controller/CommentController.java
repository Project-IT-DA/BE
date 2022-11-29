package com.example.itDa.controller;

import com.example.itDa.dto.request.CommentRequestDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto){
        return commentService.createComment(requestDto);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto){
        return commentService.updateComment(commentId, requestDto);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }

}
