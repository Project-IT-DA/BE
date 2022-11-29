package com.example.itDa.service;

import com.example.itDa.domain.model.Comment;
import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.repository.CommentRepository;
import com.example.itDa.dto.request.CommentRequestDto;
import com.example.itDa.dto.response.CommentResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityService communityService;
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto) {
        Community community = communityService.isPresentCommunity(requestDto.getCommuId());

        Comment comment = Comment.builder()
                .community(community)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .build();

        return ResponseDto.success(commentResponseDto);
    }


    @Transactional
    public ResponseDto<?> updateComment(Long commentId, CommentRequestDto requestDto) {
        Community community = communityService.isPresentCommunity(requestDto.getCommuId());

        Comment comment = isPresentComment(commentId);
        if (comment == null) {
            return ResponseDto.fail("NOT_FOUND", "해당 코멘트가 없습니다.");
        }
        comment.update(requestDto);

        return ResponseDto.success("코멘트가 수정되었습니다.");
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId) {
        Comment comment = isPresentComment(commentId);
        if (comment == null) {
            return ResponseDto.fail("NOT_FOUND", "해당 코멘트가 없습니다");
        }

        commentRepository.delete(comment);

        return ResponseDto.success("코멘트가 삭제되었습니다.");
    }
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }


}
