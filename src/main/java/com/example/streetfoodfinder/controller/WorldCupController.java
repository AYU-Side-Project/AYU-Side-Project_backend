package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.entity.FoodType;
import com.example.streetfoodfinder.service.WorldCupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("worldcup")
public class WorldCupController {

    @Autowired
    private WorldCupService worldCupService;

    @GetMapping("/randomfood")
    public ResponseEntity<FoodType[]> getRandomFood() {
        FoodType[] randomFoods = worldCupService.getRandomImages();
        return new ResponseEntity<>(randomFoods, HttpStatus.OK);
    }

    @GetMapping("/selectedfood/{index}") //선택한 음식 로직 호출
    public ResponseEntity<String> selectFood(@PathVariable int index) {
            worldCupService.processSelectedFood(index);
            return new ResponseEntity<>("Food selected successfully", HttpStatus.OK);
    }

    @GetMapping("/nextround") //다음 라운드로 진행하는 엔드포인트
    public ResponseEntity<FoodType[]> getNextRoundFood() {
        FoodType[] nextRoundFoods = worldCupService.getNextRoundImages();
        return new ResponseEntity<>(nextRoundFoods, HttpStatus.OK);
    }
}