package com.example.streetfoodfinder.service;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.LoginForm;
import com.example.streetfoodfinder.Filter.JwtTokenProvider;
import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.exception.ErrorCode;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LogInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private LoginForm loginForm;

    @Transactional
    public Token login(String email, String pw) {
        loginForm = new LoginForm();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_MEMBER));
        if (!passwordEncoder.matches(pw, member.getPw())) {
            throw new MemberException(ErrorCode.NOT_PASSWORD);
        }

        Token tokenDto = jwtTokenProvider.createAccessToken(member.getUsername(), member.getRoles());
        return tokenDto;
    }
}
