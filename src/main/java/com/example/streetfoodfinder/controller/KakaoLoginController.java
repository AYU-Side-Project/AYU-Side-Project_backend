package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.entity.MsgEntity;
import com.example.streetfoodfinder.domain.form.KakaoForm;
import com.example.streetfoodfinder.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/kakao/login")
    public ResponseEntity<String> kakaoLogin(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
        return ResponseEntity.ok("Kakao 로그인으로 이동 중...");
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<MsgEntity> kakaoLoginCallback(HttpServletRequest request) {
        try {
            // 카카오에서 전달받은 코드로 카카오 정보를 가져옴
            KakaoForm kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

            // 사용자가 이미 가입되어 있는지 확인
            Optional<Member> existingMember = kakaoService.getMemberByEmail(kakaoInfo.getEmail());

            if (existingMember.isPresent()) {
                // 기존에 가입된 사용자라면 로그인 처리
                Token tokenDto = kakaoService.login(existingMember.get().getEmail(), "default_password");
                return ResponseEntity.ok().body(new MsgEntity("로그인 성공", tokenDto));
            } else {
                // 기존에 가입된 사용자가 아니라면 회원가입 처리
                Token tokenDto = kakaoService.loginOrRegister(kakaoInfo.getEmail(), "default_password");
                return ResponseEntity.ok().body(new MsgEntity("가입 성공", tokenDto));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgEntity("에러", e.getMessage()));
        }
    }
}
