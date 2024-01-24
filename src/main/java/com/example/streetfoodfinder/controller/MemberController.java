package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.Filter.JwtTokenProvider;
import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.KakaoLoginForm;
import com.example.streetfoodfinder.domain.form.MemberUpdateForm;
import com.example.streetfoodfinder.service.MemberKakaoService;
import com.example.streetfoodfinder.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberKakaoService memberKakaoService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/kakao")
    public ResponseEntity<String> login(Model model) {
        model.addAttribute("kakaoUrl", memberKakaoService.getKakaoLogin());
        return ResponseEntity.ok(model.toString());
    }

    @GetMapping("/callback")
    public ResponseEntity<Object> callback(HttpServletRequest request) throws Exception {
        KakaoLoginForm kakaoInfo = memberKakaoService.getKakaoInfo(request.getParameter("code"));
        Member member = KakaoLoginForm.from(kakaoInfo);
        if (memberService.saveMember(member)) {
            Token token = jwtTokenProvider.createAccessToken(member.getKakaoId(), member.getRoles());
            return new ResponseEntity<>(token, HttpStatus.CREATED);//jwtoken 반환
        } else
            return new ResponseEntity<>("이미 존재하는 회원입니다.", HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete")//회원 탈퇴는 회원 탈퇴 버튼을 누를 시 카카오 로그인을 다시 진행하고 로그인에 성공하면 탈퇴
    public ResponseEntity<String> memberDelete(Model model){
        model.addAttribute("kakaoUrl", memberKakaoService.deleteKakaoUser());
        return ResponseEntity.ok(model.toString());
    }

    @GetMapping("/deletecallback")
    public ResponseEntity<Object> deleteCallback(HttpServletRequest request) throws Exception {
        KakaoLoginForm kakaoInfo = memberKakaoService.checkKakao(request.getParameter("code"));
        Member member = KakaoLoginForm.from(kakaoInfo);
        if (memberService.deleteMember(member.getKakaoId()))
            return new ResponseEntity<>("회원 탈퇴에 성공하였습니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/update/{memberId}")
    public ResponseEntity<Object> memberUpdate(@PathVariable Long memberId, @Valid @RequestBody MemberUpdateForm memberUpdateForm){
        Member member = MemberUpdateForm.from(memberUpdateForm);
        memberService.updateMember(memberId, member);
        return new ResponseEntity<>("회원 정보가 수정되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/member/{memberId}") //조회
    public ResponseEntity<Object> memberInquiry(@PathVariable Long memberId){
        Map<String, Object> map = memberService.inquiryMember(memberId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/isconsented/{memberId}")//위치정보동의 했는지 확인
    public Boolean memberIsConsented(@PathVariable Long memberId) {
        return memberService.isConsented(memberId);
    }
}