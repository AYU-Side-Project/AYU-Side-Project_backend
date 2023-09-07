package com.example.streetfoodfinder.exception;

import lombok.Getter;
@Getter
public class MemberException extends RuntimeException {
    private final ErrorCode errorCode;
    public MemberException(ErrorCode errorCode) {
        super(errorCode.getDetail());
        this.errorCode = errorCode;
    }
}
