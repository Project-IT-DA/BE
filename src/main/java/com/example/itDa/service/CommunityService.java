package com.example.itDa.service;

import com.example.itDa.domain.model.Comment;
import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.repository.CommentRepository;
import com.example.itDa.domain.repository.CommunityRepository;
import com.example.itDa.dto.request.CommunityRequestDto;
import com.example.itDa.dto.response.CommentResponseDto;
import com.example.itDa.dto.response.CommunityListResponseDto;
import com.example.itDa.dto.response.CommunityResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public ResponseDto<?> createCommunity(CommunityRequestDto requestDto) {

        Community community = Community.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(requestDto.getImgUrl())
                .build();

        communityRepository.save(community);
        CommunityResponseDto communityResponseDto = CommunityResponseDto.builder()
                .commuId(community.getCommuId())
                .title(community.getTitle())
                .content(community.getContent())
                .imgUrl(community.getImgUrl())
                .build();

        return ResponseDto.success(communityResponseDto);

    }


    @Transactional
    public ResponseDto<?> getAllCommunity() {

        List<Community> communityList = communityRepository.findAll();
        List<CommunityListResponseDto> responseDtoList = new ArrayList<>();

        for(Community community : communityList){
            int comments = commentRepository.countAllByCommunity(community);

            responseDtoList.add(CommunityListResponseDto.builder()
                    .commuId(community.getCommuId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .imgUrl(community.getImgUrl())
                    .commentsNum(comments)
                    .build());
        }

        return ResponseDto.success(responseDtoList);

    }

    @Transactional
    public ResponseDto<?> getCommunity(Long commuId) {
        Community community = isPresentCommunity(commuId);
        if (community == null){
            return ResponseDto.fail("NOT_FOUND", "해당 커뮤니티 글이 없습니다.");
        }

        List<Comment> commentList = commentRepository.findAllByCommunity(community);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getCommentId())
                            .content(community.getContent())
                            .build());
        }

        CommunityResponseDto communityResponseDto = CommunityResponseDto.builder()
                .commuId(community.getCommuId())
                .title(community.getTitle())
                .content(community.getContent())
                .imgUrl(community.getImgUrl())
                .comments(commentResponseDtoList)
                .build();

        return ResponseDto.success(communityResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateCommunity(Long commuId, CommunityRequestDto requestDto) {
        Community community = isPresentCommunity(commuId);
        if (community == null) {
            return ResponseDto.fail("NOT_FOUND", "해당 커뮤니티 글이 없습니다.");}
            community.updateCommunity(requestDto);

        return ResponseDto.success("커뮤니티 글이 수정되었습니다.");
    }


        public ResponseDto<?> deleteCommunity (Long commuId) {
            Community community = isPresentCommunity(commuId);
            if (community == null) {
                return ResponseDto.fail("NOT_FOUND", "해당 커뮤니티 글이 없습니다.");
            }
            communityRepository.deleteById(commuId);

            return ResponseDto.success("커뮤니티 글이 삭제되었습니다.");
        }

        @Transactional(readOnly = true)
        public Community isPresentCommunity(Long commuId) {
        Optional<Community> optionalCommunity = communityRepository.findById(commuId);
        return optionalCommunity.orElse(null);
    }
}
