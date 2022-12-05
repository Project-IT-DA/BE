package com.example.itDa.service;

import com.example.itDa.domain.model.Comment;
import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.CommentRepository;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.dto.request.CommentRequestDto;
import com.example.itDa.dto.response.CommentResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommunityService communityService;
    private final UserRepository userRepository;
    @Transactional
    public ResponseDto<?> createComment(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );
        Community community = communityService.isPresentCommunity(requestDto.getCommuId());

        Comment comment = Comment.builder()
                .community(community)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();

        return ResponseDto.success(commentResponseDto);
    }


    @Transactional
    public ResponseDto<?> updateComment(UserDetailsImpl userDetails, Long commentId, CommentRequestDto requestDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );
        Community community = communityService.isPresentCommunity(requestDto.getCommuId());

        Comment comment = isPresentComment(commentId);
        if (comment == null) {
            return ResponseDto.fail("NOT_FOUND", "해당 코멘트가 없습니다.");
        }
        if (comment.getUser().getEmail().equals(userDetails.getUsername())){
            comment.update(requestDto);
        }
        return ResponseDto.success("코멘트가 수정되었습니다.");
    }

    @Transactional
    public ResponseDto<?> deleteComment(UserDetailsImpl userDetails, Long commentId) {
        Comment comment = isPresentComment(commentId);
        if (comment == null) {
            return ResponseDto.fail("COMMENT_NOT_FOUND_404", "해당 코멘트가 존재하지 않습니다.");
        }

        if (comment.getUser().getEmail().equals(userDetails.getUsername())){
            commentRepository.delete(comment);
        } else {
            return ResponseDto.fail("NO_PERMISSION_TO_DELETE_NOTICE_400","삭제 권한이 없습니다.");
        }

        return ResponseDto.success("코멘트가 삭제되었습니다.");
    }
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }


}
