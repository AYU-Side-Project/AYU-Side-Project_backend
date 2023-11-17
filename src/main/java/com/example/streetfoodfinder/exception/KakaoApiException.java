package com.example.streetfoodfinder.exception;

import org.springframework.http.HttpStatus;

public class KakaoApiException extends RuntimeException {
    private HttpStatus httpStatus = null;
    private final String detail;

    public KakaoApiException(String detail) {
        this.detail = detail;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDetail() {
        return detail;
    }
}
