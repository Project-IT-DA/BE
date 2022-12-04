package com.example.itDa.service;

import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.dto.response.ProfileResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.global.exception.ErrorCode;
import com.example.itDa.infra.global.exception.RequestException;
import com.example.itDa.infra.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KakaoUserService kakaoUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkEmail(userDetails.getUser().getEmail());

        return new ResponseEntity<>(ResponseDto.success(ProfileResponseDto.of(user, documentRepository.countDocumentByUser(user))), HttpStatus.OK);

    }

    private User checkEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RequestException(ErrorCode.USER_NOT_EXIST));
    }
}
