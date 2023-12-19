package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Member;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoLoginForm {
    private Long id;
    private String email;
    private String nickname;
    private String profile;
    private String thumbNail;
    public static Member from (KakaoLoginForm from){
        return Member.builder()
                .kakaoId(from.getId())
                .email(from.getEmail())
                .nickname(from.getNickname())
                .thumbNail(from.getThumbNail())
                .profile(from.getProfile())
                .updateDate(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .build();
    }
}