package com.example.streetfoodfinder.service;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static com.example.streetfoodfinder.exception.ErrorCode.NOT_EXIST_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public Boolean saveMember(Member member) {
        boolean msg = false;
        if(memberRepository.findByKakaoId(member.getKakaoId()).isEmpty()){
            memberRepository.save(member);
            msg = true;
        }
        return msg;
    }
    @Transactional
    public Boolean deleteMember(Long kakaoId){
        boolean msg = false;
        if(memberRepository.findByKakaoId(kakaoId).isPresent()){
            memberRepository.deleteByKakaoId(kakaoId);
            msg = true;
        }
        return msg;
    }

    @Transactional
    public void updateMember(Long memberId, Member member){
        Member updatedMember = memberRepository.findByMemberId(memberId).orElseThrow(() -> new MemberException(NOT_EXIST_MEMBER));
        String nickname = member.getNickname();
        String profile = member.getProfile();
        Boolean locationInformationConsent = member.getLocationInformationConsent();

        updatedMember.setUpdateDate(member.getUpdateDate());
        if(nickname != null && !nickname.equals(""))
            updatedMember.setNickname(nickname);
        if(profile != null && !profile.equals(""))
            updatedMember.setProfile(profile);
        if(locationInformationConsent != null)
            updatedMember.setLocationInformationConsent(locationInformationConsent);
    }

    @Transactional
    public Map<String, Object> inquiryMember(Long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()-> new MemberException(NOT_EXIST_MEMBER));

        Map<String, Object> map = new HashMap<>();
        map.put("nickname", member.getNickname());
        map.put("profile", member.getProfile());
        map.put("locationInformationConsent", member.getLocationInformationConsent());
        map.put("createDate", member.getCreateDate());
        map.put("updateDate", member.getUpdateDate());
        return map;
    }

    @Transactional
    public Boolean isConsented(Long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()-> new MemberException(NOT_EXIST_MEMBER));
        return member.getLocationInformationConsent();
    }
}
