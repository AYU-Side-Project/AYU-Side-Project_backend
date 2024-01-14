package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.FoodType;
import com.example.streetfoodfinder.domain.entity.RoundFood;
import com.example.streetfoodfinder.repository.RoundFoodRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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
            "udon.jpg"
    };

    private final RoundFoodRepository roundFoodRepository;
    private FoodType food1;
    private FoodType food2;

    public WorldCupService(RoundFoodRepository roundFoodRepository) {
        this.roundFoodRepository = roundFoodRepository;
    }
    public FoodType[] getRandomImages() { //이미지 URL 배열에서 랜덤으로 두 음식을 선택하는 메서드
        Random random = new Random();
        int index1 = random.nextInt(imageUrls.length);
        int index2;

        do {
            index2 = random.nextInt(imageUrls.length);
        } while (index1 == index2);

        if (imageUrls.length > index1 && imageUrls.length > index2) { //이미지 URL 배열이 초기화 되었는지 확인
            FoodType food1 = new FoodType(s3BucketUrl + imageUrls[index1]);
            FoodType food2 = new FoodType(s3BucketUrl + imageUrls[index2]);

            return new FoodType[]{food1, food2};
        } else {
            throw new RuntimeException("Invalid index or imageUrls is not initialized properly");
        }
    }

    public void processSelectedFood(int selectedIndex) { //선택된 음식을 처리하고 MySQL 데이터베이스에 저장
        FoodType[] randomImages = getRandomImages();

        if (selectedIndex >= 0 && selectedIndex < 2) {
            FoodType selectedFood = (selectedIndex == 0) ? randomImages[0] : randomImages[1];

            if (selectedFood != null) {
                //이미지를 MySQL 데이터베이스에 저장
                RoundFood roundFood = new RoundFood();
                roundFood.setImageUrl(selectedFood.getImageUrl());
                roundFoodRepository.save(roundFood);
            }
            else {
                //selectedFood가 null인 경우에 대한 예외 처리
                throw new RuntimeException("Selected food is null");
            }
        } else {
                //선택된 인덱스가 유효하지 않은 경우에 대한 예외 처리
            throw new IllegalArgumentException("Invalid index: " + selectedIndex);
        }
    }

    //다음 라운드로 진행하는 메서드
    public FoodType[] getNextRoundImages() {
        //현재 라운드에서 이긴 이미지들 가져오기
        List<RoundFood> winners = roundFoodRepository.findAll(); // 예시로 findAll 사용, 실제로는 적절한 쿼리 사용

        //이긴 이미지가 2개 이상이어야 다음 라운드로 진행 가능
        if (winners.size() >= 2) {
            RoundFood winner1 = winners.get(0);
            RoundFood winner2 = winners.get(1);

            //다음 라운드 이미지 생성
            FoodType nextRoundFood1 = new FoodType(winner1.getImageUrl());
            FoodType nextRoundFood2 = new FoodType(winner2.getImageUrl());

            //다음 라운드 이미지를 MySQL 데이터베이스에 저장
            RoundFood nextRoundResult1 = new RoundFood(nextRoundFood1.getImageUrl(), "다음 라운드 정보 1");
            RoundFood nextRoundResult2 = new RoundFood(nextRoundFood2.getImageUrl(), "다음 라운드 정보 2");

            roundFoodRepository.save(nextRoundResult1);
            roundFoodRepository.save(nextRoundResult2);

            //현재 라운드 결과 삭제
            roundFoodRepository.deleteAll(winners);

            return new FoodType[]{nextRoundFood1, nextRoundFood2};
        }

        // 이긴 이미지가 2개 미만이면 다음 라운드 이미지를 생성할 수 없음
        return null;
    }

}
