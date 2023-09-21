package com.example.streetfoodfinder.domain.form;
import com.example.streetfoodfinder.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginForm {
    private String email;
    private String pw;
    public static Member from (LoginForm lfrom){//폼으로 받은 값을 엔티티로 전달
        return Member.builder()
                .email(lfrom.getEmail())
                .pw(lfrom.getPw())
                .build();
    }
}
