package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DeleteForm {
    private String email;
    private String pw;

    public static Member from (DeleteForm from){
        return Member.builder()
                .email(from.getEmail())
                .pw(from.getPw())
                .build();
    }
}
