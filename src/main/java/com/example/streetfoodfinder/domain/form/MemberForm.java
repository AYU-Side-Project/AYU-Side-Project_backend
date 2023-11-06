package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class MemberForm {
    public static Member from(KakaoForm kakaoInfo, String encryptPw) {
        return Member.builder()
                .nickname(kakaoInfo.getNickname())
                .email(kakaoInfo.getEmail())
                .password(encryptPw)
                .build();
    }
}