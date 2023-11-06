package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindMemberService implements UserDetailsService {

    public final MemberRepository memberRepository; // 사용자 정보를 데이터베이스에서 가져오는 데 사용할 Repository

    public FindMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        // 사용자 정보를 데이터베이스에서 찾아오는 예시 (MemberRepository를 사용)
        Member member = memberRepository.findByNickname(nickname);

        if (member == null) {
            throw new UsernameNotFoundException("User not found with email: " + nickname);
        }

        // UserDetails 객체를 생성하고 반환
        return User.withUsername(member.getNickname())
                .password(member.getPassword())
                .roles("USER") // 사용자 역할
                .build();
    }
}
