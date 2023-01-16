package com.example.itDa.infra.global.exception;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "EXCEPTIONS")
public class ExceptionController {

    @GetMapping("/exception")
    public ResponseEntity<?> exceptionTest() {
        throw new RequestException(ErrorCode.USER_INFO_NOT_FORMATTED);
    }
}

