package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.UpdateForm;
import com.example.streetfoodfinder.service.UpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/memberInfo")
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PutMapping("/update")
    public ResponseEntity<String> updateMember(@RequestBody @Valid UpdateForm form) {
        updateService.updateMember(form);
        return ResponseEntity.ok("회원 정보가 업데이트되었습니다.");
    }
}