package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.Filter.JwtTokenProvider;
import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.KakaoLoginForm;
import com.example.streetfoodfinder.service.MemberKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
@RequestMapping("kakao")
public class MemberKakaoController {

    private final MemberKakaoService memberKakaoService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/")
    public ResponseEntity<String> login(Model model) {
        model.addAttribute("kakaoUrl", memberKakaoService.getKakaoLogin());
        return ResponseEntity.ok(model.toString());
    }

    @GetMapping("/callback")
    public ResponseEntity<Object> callback(HttpServletRequest request) throws Exception {
        KakaoLoginForm kakaoInfo = memberKakaoService.getKakaoInfo(request.getParameter("code"));
        Member member = KakaoLoginForm.from(kakaoInfo);
        if (memberKakaoService.saveMember(member)) {
            Token token = jwtTokenProvider.createAccessToken(member.getKakaoId().toString(), member.getRoles());
            return new ResponseEntity<>(token, HttpStatus.CREATED);//jwtoken 반환
        } else
            return new ResponseEntity<>("이미 존재하는 회원입니다.", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete")//회원 탈퇴는 회원 탈퇴 버튼을 누를 시 카카오 로그인을 다시 진행하고 로그인에 성공하면 탈퇴
    public ResponseEntity<String> memberDelete(HttpServletRequest request, Model model) throws Exception {
        model.addAttribute("kakaoUrl", memberKakaoService.deleteKakaoUser());
        return ResponseEntity.ok(model.toString());
    }

    @GetMapping("/deletecallback")
    public ResponseEntity<Object> deleteCallback(HttpServletRequest request) throws Exception {
        KakaoLoginForm kakaoInfo = memberKakaoService.checkKakao(request.getParameter("code"));
        Member member = KakaoLoginForm.from(kakaoInfo);
        if (memberKakaoService.deleteMember(member.getKakaoId()))
            return new ResponseEntity<>("회원 탈퇴에 성공하였습니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
    }
}