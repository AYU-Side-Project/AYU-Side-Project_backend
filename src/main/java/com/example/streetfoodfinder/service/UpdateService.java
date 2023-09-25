package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.UpdateForm;
import com.example.streetfoodfinder.exception.ErrorCode;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateMember(UpdateForm form) {
        // 이메일을 사용하여 회원 찾기
        Member member = memberRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_EXIST_MEMBER));

        if (!passwordEncoder.matches(form.getCurrentPw(), member.getPw())) {
            throw new MemberException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
        else if (!(form.getPw().length() < 8)) {
            // 비밀번호 8글자 이상일 때, 수정된 정보로 회원 엔터티 업데이트
            String encryptedPassword = passwordEncoder.encode(form.getPw()); // 암호화
            member.setContact(form.getContact());
            member.setPw(encryptedPassword);
            member.setName(form.getName());
        }
        else {
            throw new MemberException(ErrorCode.NOT_VALID_PASSWORD);
        }
        memberRepository.save(member);
    }
}
