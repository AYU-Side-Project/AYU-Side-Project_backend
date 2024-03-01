package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.FoodType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WorldCupService {

    @Value("${s3.bucket.url}")
    private String s3BucketUrl;

    private final String[] imageUrls = {
            "chicken.jpg",
            "hamburger.jpg",
            "jjajangmeon.jpg",
            "pizza.jpg",
            "bulgogi.jpg",
            "kimbap.jpg",
            "ramyeon.jpg",
            "samgyeopsal.jpg",
            "susi.jpg",
            "toppokki.jpg",
            "udon.jpg",
            "jjimdark.jpg",
            "naengmyeon.jpg",
            "gejang.jpg",
            "kimchibokkeumbab.jpg",
            "darkgalbi.jpg"
    };

    public FoodType[] getRandomImages() {
        int[] randomIndices = getRandomIndices();
        FoodType food1 = new FoodType(s3BucketUrl + imageUrls[randomIndices[0]]);
        FoodType food2 = new FoodType(s3BucketUrl + imageUrls[randomIndices[1]]);

        return new FoodType[]{food1, food2};
    }

    private int[] getRandomIndices() {
        Random random = new Random();
        int index1 = random.nextInt(imageUrls.length);
        int index2;

        do {
            index2 = random.nextInt(imageUrls.length);
        } while (index1 == index2);

        return new int[]{index1, index2};
    }

}
