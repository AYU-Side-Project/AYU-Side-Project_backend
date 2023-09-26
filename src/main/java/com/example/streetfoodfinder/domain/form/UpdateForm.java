package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateForm {
    private String email;              // 회원의 이메일
    private String currentPw;    // 현재 비밀번호
    private String contact;            // 수정할 전화번호
    private String pw;                 // 수정할 비밀번호
    private String name;               // 수정할 멤버 이름

    public static Member from (UpdateForm from){
        return Member.builder()
                .contact(from.getContact())
                .pw(from.getPw())
                .name(from.getName())
                .build();
    }

}
