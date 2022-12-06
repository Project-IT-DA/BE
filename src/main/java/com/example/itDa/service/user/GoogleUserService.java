package com.example.itDa.service.user;

import com.example.itDa.domain.model.User;
import com.example.itDa.domain.model.UserSocialEnum;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.dto.response.GoogleSocialDto;
import com.example.itDa.dto.response.LoginDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import com.example.itDa.infra.security.jwt.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.example.itDa.infra.security.handler.AuthenticationSuccessHandler.AUTH_HEADER;
import static com.example.itDa.infra.security.handler.AuthenticationSuccessHandler.TOKEN_TYPE;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class GoogleUserService {
    @Value("${google.login.client-id}")
    private String CLIENT_ID;

    @Value("${google.login.client-secret}")
    private String CLIENT_SECRET;

    @Value("${google.login.redirect-uri}")
    private String REDIRECT_URI;


    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    //구글 로그인
    public ResponseDto<LoginDto> googleLogin(String code, HttpServletResponse response) throws IOException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. "액세스 토큰"으로 "구글 사용자 정보" 가져오기
        GoogleSocialDto googleSocialDto = getGoogleUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User googleUser = registerGoogleUser(googleSocialDto);

        // 4. 토큰 발급
        googleLoginAccess(googleUser, response);


        return ResponseDto.success(
                LoginDto.builder()
                        .nickname(googleUser.getNickname())
                        .email(googleUser.getEmail())
                        .profileImg(googleUser.getProfileImg())
                        .build()
        );

    }

    private void googleLoginAccess(User googleUser, HttpServletResponse response) {

        UserDetailsImpl userDetails = new UserDetailsImpl(googleUser);
        String token = JwtTokenUtils.generateJwtToken(userDetails);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

    }

    private User registerGoogleUser(GoogleSocialDto googleSocialDto) {

        // User 정보있는지 확인
        User googleUser = userRepository.findByEmail(googleSocialDto.getEmail()).orElse(null);

        // User 정보가 없으면 회원가입 시키기
        if (googleUser == null) {
            String password = UUID.randomUUID().toString();

            googleUser = User.builder()
                    .socialId(googleSocialDto.getGoogleId())
                    .nickname(googleSocialDto.getNickname())
                    .password(encoder.encode(password))
                    .email(googleSocialDto.getEmail())
                    .social(UserSocialEnum.GOOGLE)
                    .profileImg(googleSocialDto.getProfileImg())
                    .build();

            userRepository.save(googleUser);
        }

        return googleUser;

    }

    private String getAccessToken(String code) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );


        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }

    private GoogleSocialDto getGoogleUserInfo(String accessToken) throws IOException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기 -> 구글한테 보내는거
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://accounts.google.com/o/oauth2/v2/auth",
                HttpMethod.GET,
                googleUserInfoRequest,
                String.class
        );
        // rt.exchange하면 response에 밑에 것들이 들어간다.

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper(); // 객체로 만들어줌

        JsonNode jsonNode = objectMapper
                .readTree(responseBody);

        Long googleId = jsonNode.get("sub").asLong();

        String nickname = jsonNode
                .get("userinfo")
                .get("name").asText();

        String profileImg = jsonNode
                .get("userinfo")
                .get("picture").asText();

        String email = jsonNode
                .get("userinfo")
                .get("email").asText();

        return GoogleSocialDto.builder()
                .googleId(googleId)
                .email(email)
                .nickname(nickname)
                .profileImg(profileImg)
                .build();
    }

}
