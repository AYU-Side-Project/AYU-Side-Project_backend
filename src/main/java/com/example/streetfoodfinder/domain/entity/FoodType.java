package com.example.streetfoodfinder.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class FoodType {
    private static final String S3BUCKETURL = "https://streetfoodfinder.s3.ap-northeast-2.amazonaws.com/worldcup/";

    private String imageUrl;

    public FoodType(String imageName) {
        this.imageUrl = imageName;
    }

    public String getFoodImageUrl(String foodName) {
        return S3BUCKETURL + foodName + ".jpg";
    }
}
