package com.example.streetfoodfinder.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RoundFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String nextRoundInfo; // 다음 라운드 정보 저장 필드

    public RoundFood() { // JPA 엔티티 기본 생성자
    }

    public RoundFood(String imageUrl, String nextRoundInfo) { // 이미지 URL과 다음 라운드 정보를 받아 객체를 초기화
        this.imageUrl = imageUrl;
        this.nextRoundInfo = nextRoundInfo;
    }

}
