package com.example.streetfoodfinder.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({MemberException.class})
    public ResponseEntity<ExceptionResponse> UserRequestException(final MemberException memberException) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(memberException.getMessage(), memberException.getErrorCode()));
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
        private ErrorCode errorCode;
    }
}

