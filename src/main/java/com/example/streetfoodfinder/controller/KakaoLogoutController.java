package com.example.streetfoodfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.streetfoodfinder.service.KakaoLogoutService;

@RestController
public class KakaoLogoutController {

    private final KakaoLogoutService kakaoLogoutService;

    @Autowired
    public KakaoLogoutController(KakaoLogoutService kakaoLogoutService) {
        this.kakaoLogoutService = kakaoLogoutService;
    }

    @GetMapping("/kakao/logout")
    public ResponseEntity<String> logoutKakaoUser(@RequestParam("accessToken") String accessToken) {
        boolean logoutSuccess = kakaoLogoutService.logoutKakaoUser(accessToken);

        if (logoutSuccess) {
            return ResponseEntity.ok("Kakao user logged out successfully.");
        } else {
            return ResponseEntity.badRequest().body("Kakao user logout failed.");
        }
    }
}
