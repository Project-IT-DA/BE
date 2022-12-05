package com.example.itDa.infra.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"권한이 없습니다"),

    USER_DUPLICATED(HttpStatus.CONFLICT, "중복된 아이디입니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),

    COMMON_BAD_REQUEST_400(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    COMMON_INTERNAL_ERROR_500(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생하였습니다."),

    // JWT 관련
    JWT_BAD_TOKEN_400(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    JWT_UNAUTHORIZED_401(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    JWT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "유효한 JWT 토큰이 없습니다."),
    JWT_NOT_ALLOWED_405(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 JWT 토큰입니다."),

    // USER 관련
    USER_INFO_NOT_FORMATTED(HttpStatus.NOT_ACCEPTABLE, ""),


    //수정 필요
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "User이름을 찾을 수 없습니다"),

    // Article 관련
    ARTICLE_NOT_FOUND_404(HttpStatus.NOT_FOUND, "요청한 거래글 ID가 없습니다."),
    ARTICLE_DUPLICATION_409(HttpStatus.CONFLICT, "이미 등록된 거래글 ID 입니다."),

    // Community 관련
    COMMUNITY_NOT_FOUND_404(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),

    // Comment 관련
    COMMENT_NOT_FOUND_404(HttpStatus.NOT_FOUND, "해당 코멘트가 존재하지 않습니다."),

    // Role 관련
    NO_PERMISSION_TO_WRITE_NOTICE_400(HttpStatus.FORBIDDEN, "작성 권한이 없습니다."),
    NO_PERMISSION_TO_MODIFY_NOTICE_400(HttpStatus.FORBIDDEN, "수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_NOTICE_400(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다."),;

    private HttpStatus httpStatus;
    private String message;
}
