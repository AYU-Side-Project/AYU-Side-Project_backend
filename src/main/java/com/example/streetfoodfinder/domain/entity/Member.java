package com.example.streetfoodfinder.domain.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column
    @NotBlank(message = "이메일을 입력하시오.")
    @Email(message = "이메일 형식을 맞추세요.")
    private String email;
    @Column
    @NotBlank(message = "비밀번호를 입력하시오.")
    @Pattern(regexp="^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "특수문자 포함한 8글자 이상의 비밀번호를 입력하시오.(사용가능 특수문자 : !@#$%^&*=")
    private String pw;
    @Column
    @NotBlank(message = "이름을 입력하시오.")
    private String name;
    @Column
    @NotBlank(message = "전화번호를 입력하시오.")
    private String contact;

    // private Image profile;//이미지 저장 구현 필요

    @NotBlank
    private LocalTime createDate;
    @NotBlank
    private LocalTime updateDate;

    @Column
    private String verification_code;//인증코드
    @Column
    private boolean certified;//인증됨

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



