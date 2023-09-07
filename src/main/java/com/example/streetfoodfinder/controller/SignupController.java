package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.SignupForm;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor

public class SignupController {
    private final SignupService signupService;

    @PostMapping("/member")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupForm form) throws MemberException {
        signupService.Signup(form);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyMember(@RequestParam String email , @RequestParam String code){
        return ResponseEntity.ok(signupService.verifyMember(email,code));
    }
}
