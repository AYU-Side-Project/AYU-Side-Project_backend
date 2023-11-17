package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class KakaoLogoutController {

    private final KakaoService kakaoLogoutService;

    @GetMapping("/kakao/logout")
    public ResponseEntity<String> logoutKakaoUser(HttpServletRequest request) {
        String accessToken = request.getParameter("accessToken");
        boolean logoutSuccess = kakaoLogoutService.logoutKakaoUser(accessToken);

        if (logoutSuccess) {
            return ResponseEntity.ok("Kakao 사용자가 성공적으로 로그아웃되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("Kakao 사용자 로그아웃 실패.");
        }
    }
}
