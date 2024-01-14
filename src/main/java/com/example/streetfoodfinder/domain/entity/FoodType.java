package com.example.streetfoodfinder.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class FoodType {
    private static final String S3BUCKETURL = "https://sft-worldcup.s3.ap-northeast-2.amazonaws.com/food/"; //S3 버킷 URL 상수 선언

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; //음식 이미지 URL

    public FoodType() { //JPA 엔티티 기본 생성자

    }

    public FoodType(String imageName) {
        this.imageUrl = imageName; //이미지 이름 받아 음식 객체 생성
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getFoodImageUrl(String foodName) {     // 음식 이미지 URL 출력
        return S3BUCKETURL + foodName + ".jpg";
    }
}
