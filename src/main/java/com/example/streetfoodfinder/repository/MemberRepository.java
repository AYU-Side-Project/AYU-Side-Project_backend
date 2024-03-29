package com.example.streetfoodfinder.repository;

import com.example.streetfoodfinder.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(Long memberId);
    Optional<Member> findByKakaoId(Long kakaoId);
    Optional<Member> findByEmail(String email);
    void deleteByKakaoId(Long kakaoId);
}