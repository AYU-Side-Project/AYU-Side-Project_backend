package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Member;
import lombok.*;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupForm {
    private String email;
    private String pw;
    private String name;
    private String contact;

    public static Member from (SignupForm from){
        return Member.builder()
                .email(from.getEmail())
                .pw(from.getPw())
                .name(from.getName())
                .contact(from.getContact())
                .build();
    }
}