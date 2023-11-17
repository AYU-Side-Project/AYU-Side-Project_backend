package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.DeleteForm;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rud")
@RequiredArgsConstructor
public class DeleteController {
    private final KakaoService deleteService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> memberDelete(@RequestBody @Valid DeleteForm form) throws MemberException {
        deleteService.unlinkKakaoAccount(form.getAccessToken());
        deleteService.deleteMember(form.toMember());
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
    }
}
