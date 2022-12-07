package com.example.itDa.service;

import com.example.itDa.domain.model.Comment;
import com.example.itDa.domain.model.Community;
import com.example.itDa.domain.model.CommunityFile;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.CommentRepository;
import com.example.itDa.domain.repository.CommunityFileRepository;
import com.example.itDa.domain.repository.CommunityRepository;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.dto.request.CommunityRequestDto;
import com.example.itDa.dto.response.CommentResponseDto;
import com.example.itDa.dto.response.CommunityListResponseDto;
import com.example.itDa.dto.response.CommunityResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.s3.S3UploaderService;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final S3UploaderService s3UploaderService;
    private final CommunityFileRepository communityFileRepository;
    @Transactional
    public ResponseDto<?> createCommunity(UserDetailsImpl userDetails, MultipartFile[] multipartFiles, CommunityRequestDto requestDto) {

        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );

        Community community = Community.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(user)
                .build();

        communityRepository.save(community);

        List<String> imgUrls;
        try {
            imgUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "community");
        } catch (IOException e){
            throw new RuntimeException();
        }

        List<String> imgNames = new ArrayList<>();
        List<CommunityFile> communityFiles = new ArrayList<>();

        if(imgUrls != null){
            for(int i = 0; i < imgUrls.size(); i++){
                imgNames.add(multipartFiles[i].getOriginalFilename());
                communityFiles.add(CommunityFile.builder()
                        .community(community)
                        .imgName(multipartFiles[i].getOriginalFilename())
                        .imgUrl(imgUrls.get(i))
                        .build());
            }
        }
        communityFileRepository.saveAll(communityFiles);

        CommunityResponseDto communityResponseDto = CommunityResponseDto.builder()
                .userId(user.getId())
                .commuId(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .imgUrls(imgUrls)
                .imgNames(imgNames)
                .build();

        return ResponseDto.success(communityResponseDto);

    }


    @Transactional
    public ResponseDto<?> getAllCommunity() {

        List<Community> communityList = communityRepository.findAll();
        List<CommunityListResponseDto> responseDtoList = new ArrayList<>();


        for(Community community : communityList){
            int comments = commentRepository.countAllByCommunity(community);
            List<String> imgUrls = new ArrayList<>();
            List<String> imgNames = new ArrayList<>();
            List<CommunityFile> communityFiles = communityFileRepository.findAllByCommunityId(community.getId());
            for (CommunityFile communityFile : communityFiles) {
                imgUrls.add(communityFile.getImgUrl());
                imgNames.add(communityFile.getImgName());
            }
            responseDtoList.add(CommunityListResponseDto.builder()
                    .commuId(community.getId())
                    .userId(community.getUser().getId())
                    .title(community.getTitle())
                    .content(community.getContent())
                    .imgUrls(imgUrls)
                    .imgNames(imgNames)
                    .commentsNum(comments)
                    .build());
        }

        return ResponseDto.success(responseDtoList);

    }

    @Transactional
    public ResponseDto<?> getCommunity(Long commuId) {
        Community community = isPresentCommunity(commuId);
        if (community == null){
            return ResponseDto.fail("COMMUNITY_NOT_FOUND_404", "해당 게시글이 존재하지 않습니다.");
        }

        List<Comment> commentList = commentRepository.findAllByCommunity(community);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        List<String> imgUrls = new ArrayList<>();
        List<String> imgNames = new ArrayList<>();
        List<CommunityFile> communityFile = communityFileRepository.findAllByCommunityId(community.getId());
        for (CommunityFile file : communityFile) {
            imgUrls.add(file.getImgUrl());
            imgNames.add(file.getImgName());
        }
        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getId())
                            .content(community.getContent())
                            .build());
        }

        CommunityResponseDto communityResponseDto = CommunityResponseDto.builder()
                .commuId(community.getId())
                .userId(community.getUser().getId())
                .title(community.getTitle())
                .content(community.getContent())
                .imgUrls(imgUrls)
                .imgNames(imgNames)
                .comments(commentResponseDtoList)
                .build();

        return ResponseDto.success(communityResponseDto);
    }

    @Transactional
    public ResponseDto<?> updateCommunity(Long commuId, UserDetailsImpl userDetails, CommunityRequestDto requestDto) {
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );

        Community community = isPresentCommunity(commuId);
        if (community == null) {
            return ResponseDto.fail("COMMUNITY_NOT_FOUND_404", "해당 커뮤니티 글이 없습니다.");}

        if(community.getUser().getEmail().equals(user.getEmail())){
            community.updateCommunity(requestDto);}

        return ResponseDto.success("커뮤니티 글이 수정되었습니다.");
    }


    public ResponseDto<?> deleteCommunity (Long commuId, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_EXIST)
        );

        Community community = isPresentCommunity(commuId);
        if (community == null) {
            return ResponseDto.fail("NOT_FOUND", "해당 커뮤니티 글이 없습니다.");
        }
        if(community.getUser() != user) {
            return ResponseDto.fail("BAD_REQUEST", "글쓴이만 삭제 할 수 있습니다.");
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
