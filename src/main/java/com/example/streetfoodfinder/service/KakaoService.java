package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.Filter.JwtTokenProvider;
import com.example.streetfoodfinder.Filter.Token;
import com.example.streetfoodfinder.domain.entity.Member;
import com.example.streetfoodfinder.domain.form.KakaoForm;
import com.example.streetfoodfinder.exception.ErrorCode;
import com.example.streetfoodfinder.exception.KakaoApiException;
import com.example.streetfoodfinder.exception.MemberException;
import com.example.streetfoodfinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;

    @Value("e2bb3928b30e2b95f39bf7b508b80c52")
    private String KAKAO_CLIENT_ID;

    @Value("$NgNHmFWa4ZJYPpoRwq7Gsq8j9xR0NzRg")
    private String KAKAO_CLIENT_SECRET;

    @Value("http://localhost:8080/kakao/oauth")
    private String KAKAO_REDIRECT_URL;

    @Value("c5f8ac3466d1e2a40aa0e385942576b6")
    private String KAKAO_ADMIN_KEY;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";
    private static final String KAKAO_LOGOUT_URL = "https://kauth.kakao.com/oauth/logout";
    private static final String KAKAO_UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    @Transactional
    public Token login(String email, String pw) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_EXIST_MEMBER));
        if (!passwordEncoder.matches(pw, member.getPassword())) {
            throw new MemberException(ErrorCode.NOT_CORRECT_PASSWORD);
        }

        Token tokenDto = jwtTokenProvider.createAccessToken(member.getNickname());
        return tokenDto;
    }

    public String getKakaoLogin() {
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public KakaoForm getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code");

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code"         , code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URL);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

            accessToken  = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");
        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        return getUserInfoWithToken(accessToken);
    }

    private KakaoForm getUserInfoWithToken(String accessToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject account = (JSONObject) jsonObj.get("kakao_account");
            JSONObject profile = (JSONObject) account.get("profile");

            long id = (long) jsonObj.get("id");
            String email = String.valueOf(account.get("email"));
            String nickname = String.valueOf(profile.get("nickname"));

            return KakaoForm.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname).build();
        } else {
            throw new Exception("Kakao API call failed with status: " + response.getStatusCode());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String kakaoUserId) throws UsernameNotFoundException {
        // Kakao API를 사용하여 사용자 정보를 가져오는 로직
        Map<String, ?> kakaoUserInfo = retrieveKakaoUserInfo(kakaoUserId);

        // 사용자 정보를 기반으로 UserDetails 객체 생성
        return buildUserDetails(kakaoUserInfo);
    }

    private Map<String, ?> retrieveKakaoUserInfo(String kakaoUserId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoUserId);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
                JSONObject account = (JSONObject) jsonObj.get("kakao_account");
                JSONObject profile = (JSONObject) account.get("profile");

                long id = (long) jsonObj.get("id");
                String email = String.valueOf(account.get("email"));
                String nickname = String.valueOf(profile.get("nickname"));

                return Map.of("id", id, "email", email, "nickname", nickname);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse Kakao user info response", e);
            }
        } else {
            throw new RuntimeException("Kakao API call failed with status: " + response.getStatusCode());
        }
    }

    private UserDetails buildUserDetails(Map<String, ?> kakaoUserInfo) {
        String email = (String) kakaoUserInfo.get("email");
        String nickname = (String) kakaoUserInfo.get("nickname");

        // 사용자의 역할(role)을 설정할 수 있습니다. 여기서는 "ROLE_USER"로 설정합니다.
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        // UserDetails 객체 생성
        return new User(email, "", authorities);
    }


    public boolean logoutKakaoUserByAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOGOUT_URL);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getStatusCode() == HttpStatus.OK;
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Token loginOrRegister(String email, String defaultPassword) {
        Token tokenDto = login(email, defaultPassword);
        return tokenDto;
    }

    public boolean logoutKakaoUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOGOUT_URL);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getStatusCode() == HttpStatus.OK;
    }


    public void unlinkKakaoAccount(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", "<사용자의 회원번호>");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_UNLINK_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode().isError()) {
            throw new KakaoApiException("Kakao API 연결 해제 중 오류가 발생했습니다. 상태 코드: " + response.getStatusCode());
        }

    }

    public void deleteMember(Member member) throws MemberException {
        Member existingMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_EXIST_MEMBER));

        memberRepository.delete(existingMember);
    }
}
