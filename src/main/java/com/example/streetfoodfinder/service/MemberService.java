package com.example.streetfoodfinder.service;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

import static com.example.streetfoodfinder.exception.ErrorCode.NOT_EXIST_MEMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Boolean saveMember(Member member) {
        if(memberRepository.findByKakaoId(member.getKakaoId()).isEmpty()){
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    public Boolean deleteMember(Long kakaoId){
        boolean msg = false;
        if(memberRepository.findByKakaoId(kakaoId).isPresent()){
            memberRepository.deleteByKakaoId(kakaoId);
            msg = true;
        }
        return msg;
    }

    public void updateMember(Long memberId, Member member){
        Member updatedMember = memberRepository.findByMemberId(memberId).orElseThrow(() -> new MemberException(NOT_EXIST_MEMBER));

        updatedMember.setUpdateDate();
        updatedMember.updateNickname(member.getNickname());
        updatedMember.updateProfile(member.getProfile());
        updatedMember.updateLocationInformationConsent(member.getLocationInformationConsent());
    }

    public Member inquiryMember(Long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()-> new MemberException(NOT_EXIST_MEMBER));

        return Member.builder()
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .locationInformationConsent(member.getLocationInformationConsent())
                .updateDate(member.getUpdateDate())
                .createDate(member.getCreateDate())
                .build();
    }

    public Boolean isConsented(Long memberId){
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(()-> new MemberException(NOT_EXIST_MEMBER));
        return member.getLocationInformationConsent();
    }
}
