package com.example.streetfoodfinder.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler({MemberException.class})
    public ResponseEntity<ExceptionErrorCodeResponse> userRequestException(final MemberException memberException) {
        return ResponseEntity.badRequest().body(new ExceptionErrorCodeResponse(memberException.getMessage(), memberException.getErrorCode()));
    }
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionStatusResponse> handleConstraintViolation(final ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ExceptionStatusResponse(ex.getConstraintViolations().iterator().next().getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler({Exception.class})//기타 모든 예외 처리
    public ResponseEntity<ExceptionStatusResponse> handleAllExceptions(final Exception ex) {
        String message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionStatusResponse(message, HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionErrorCodeResponse {
        private String message;
        private ErrorCode errorCode;
    }

    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionStatusResponse {
        private String message;
        private int status;
    }
}

