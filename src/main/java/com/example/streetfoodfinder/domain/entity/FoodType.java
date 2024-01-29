package com.example.streetfoodfinder.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodType {
    private static final String S3BUCKETURL = "https://sft-worldcup.s3.ap-northeast-2.amazonaws.com/food/"; // S3 버킷 URL 상수 선언

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // 음식 이미지 URL

    public FoodType(String imageName) {
        this.imageUrl = imageName; // 이미지 이름 받아 음식 객체 생성
    }

    public String getFoodImageUrl(String foodName) { // 음식 이미지 URL 출력
        return S3BUCKETURL + foodName + ".jpg";
    }
}
