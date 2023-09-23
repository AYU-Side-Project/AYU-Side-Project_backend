package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.DeleteForm;
import com.example.streetfoodfinder.exception.ErrorCode;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class DeleteService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = null;

    //public boolean isMemberWithdraw(String email, String pw){return memberRepository.findByEmail(email).isPresent();}

    @Transactional
    public void deleteMember(DeleteForm deleteForm) {
        Member member = memberRepository.findByEmail(deleteForm.getEmail()).orElseThrow(
                () -> new MemberException(ErrorCode.NOT_EXIST_MEMBER)
        );

        if (passwordEncoder.matches(deleteForm.getPw(), member.getPw())) {
            memberRepository.delete(member);
        } else {
            throw new MemberException(ErrorCode.NOT_CORRECT_PASSWORD);
        }
    }
}