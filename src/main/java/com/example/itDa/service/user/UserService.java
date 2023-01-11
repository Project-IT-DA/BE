package com.example.itDa.service.user;

import com.example.itDa.domain.repository.ArticleRepository;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.CommunityRepository;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.dto.request.UpdateProfileDto;
import com.example.itDa.dto.response.ProfileResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.s3.S3UploaderService;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommunityRepository communityRepository;
    private final S3UploaderService s3UploaderService;

    public ResponseDto<ProfileResponseDto> getUserProfile(UserDetailsImpl userDetails) {
        return ResponseDto.success(ProfileResponseDto.of(userDetails, articleRepository, communityRepository));
    }

    public ResponseDto<ProfileResponseDto> updateUserProfile(UserDetailsImpl userDetails,
                                                             MultipartFile multipartFile,
                                                             UpdateProfileDto updateProfileDto) {
        try {
            s3UploaderService.uploadFormDataFile(multipartFile, "profile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = checkId(userDetails.getUser().getId());
        user.setProfileImg(updateProfileDto.getImgUrl());
        user.setUsername(updateProfileDto.getUsername());
        userRepository.save(user);

        return ResponseDto.success(ProfileResponseDto.of(userDetails, articleRepository, communityRepository));
    }

    private User checkId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_EXIST));
    }

    @Transactional
    public ResponseDto<String> signOut(User user) {
            userRepository.delete(user);
            log.info("나와 :" );
        return ResponseDto.success("회원탈퇴 성공");
    }
}
