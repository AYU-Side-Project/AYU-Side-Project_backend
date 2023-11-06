package com.example.streetfoodfinder.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoLogoutService {

    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    private RestTemplate restTemplate;

    public KakaoLogoutService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}
