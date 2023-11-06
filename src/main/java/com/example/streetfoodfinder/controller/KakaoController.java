package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.entity.MsgEntity;
import com.example.streetfoodfinder.domain.form.KakaoForm;
import com.example.streetfoodfinder.domain.form.MemberForm;
import com.example.streetfoodfinder.repository.MemberRepository;
import com.example.streetfoodfinder.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class KakaoController {
    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<String> login(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
        return ResponseEntity.ok("Your Response Message Here"); // Replace with your response
    }

    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) {
        try {
            KakaoForm kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

            String encrypt_pw = passwordEncoder.encode(kakaoInfo.getEmail());
            Member member = MemberForm.from(kakaoInfo, encrypt_pw);
            memberRepository.save(member);

            return ResponseEntity.ok()
                    .body(new MsgEntity("Success", kakaoInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgEntity("Error", e.getMessage()));
        }
    }
}