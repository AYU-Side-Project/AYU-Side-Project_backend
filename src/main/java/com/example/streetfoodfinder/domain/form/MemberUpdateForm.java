package com.example.streetfoodfinder.domain.form;
import com.example.streetfoodfinder.domain.entity.Member;
import lombok.Builder;

@Builder
public class MemberUpdateForm {
    private String nickname;
    private String profile;
    private Boolean locationInformationConsent;

    public static Member from (MemberUpdateForm from){
        return Member.builder()
                .nickname(from.nickname)
                .profile(from.profile)
                .locationInformationConsent(from.locationInformationConsent)
                .build();
    }
}
