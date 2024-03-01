package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.FoodType;
import com.example.streetfoodfinder.domain.entity.RoundFood;
import com.example.streetfoodfinder.repository.RoundFoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
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
            "udon.jpg"
    };

    private final RoundFoodRepository roundFoodRepository;

    public WorldCupService(RoundFoodRepository roundFoodRepository) {
        this.roundFoodRepository = roundFoodRepository;
    }

    public FoodType[] getRandomImages() {
        Random random = new Random();
        int index1 = random.nextInt(imageUrls.length);
        int index2;

        do {
            index2 = random.nextInt(imageUrls.length);
        } while (index1 == index2);

        if (imageUrls.length > index1 && imageUrls.length > index2) {
            FoodType food1 = new FoodType(s3BucketUrl + imageUrls[index1]);
            FoodType food2 = new FoodType(s3BucketUrl + imageUrls[index2]);

            return new FoodType[]{food1, food2};
        } else {
            return new FoodType[0];
        }
    }

    public void processSelectedFood(int selectedIndex) {
        FoodType[] randomImages = getRandomImages();

        if (selectedIndex >= 0 && selectedIndex < 2) {
            FoodType selectedFood = (selectedIndex == 0) ? randomImages[0] : randomImages[1];

            if (selectedFood != null) {
                RoundFood roundFood = new RoundFood();
                roundFood.setImageUrl(selectedFood.getImageUrl());
                roundFoodRepository.save(roundFood);
            } else {
                throw new RuntimeException("Selected food is null");
            }
        } else {
            throw new IllegalArgumentException("Invalid index: " + selectedIndex);
        }
    }

    public FoodType[] getNextRoundImages() {
        List<RoundFood> winners = roundFoodRepository.findAll();

        if (winners.size() >= 2) {
            RoundFood winner1 = winners.get(0);
            RoundFood winner2 = winners.get(1);

            FoodType nextRoundFood1 = new FoodType(winner1.getImageUrl());
            FoodType nextRoundFood2 = new FoodType(winner2.getImageUrl());

            RoundFood nextRoundResult1 = new RoundFood(nextRoundFood1.getImageUrl(), "다음 라운드 정보 1");
            RoundFood nextRoundResult2 = new RoundFood(nextRoundFood2.getImageUrl(), "다음 라운드 정보 2");

            roundFoodRepository.save(nextRoundResult1);
            roundFoodRepository.save(nextRoundResult2);

            roundFoodRepository.deleteAll(winners);

            return new FoodType[]{nextRoundFood1, nextRoundFood2};
        }

        return null;
    }
}
