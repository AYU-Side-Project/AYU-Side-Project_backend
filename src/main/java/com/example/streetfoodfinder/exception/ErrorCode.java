package com.example.streetfoodfinder.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ALREADY_EXSISTS_EMAIL(HttpStatus.BAD_REQUEST,"이미 존재하는 회원입니다."),

    NOT_MEMBER(HttpStatus.BAD_REQUEST,"존재하지 않는 회원입니다."),

    NOT_PASSWORD(HttpStatus.BAD_REQUEST,"비밀번호가 틀렸습니다."),

    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST,"8글자 이상의 패스워드를 입력하시오."),

    NOT_EQUALS_CODE(HttpStatus.BAD_REQUEST, "잘못된 코드입니다.");

    private final HttpStatus httpStatus;

    private final String detail;
}
