package com.example.streetfoodfinder.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private String password;

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private Long id;
        private String nickname;
        private String email;
        private String password;

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String passsword) {
            this.password = password;
            return this;
        }

        public Member build() {
            Member member = new Member();
            member.id = this.id;
            member.nickname = this.nickname;
            member.email = this.email;
            member.password = this.password;
            return member;
        }
    }
}
