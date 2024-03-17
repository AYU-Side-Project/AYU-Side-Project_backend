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

    private final WorldCupService worldCupService;
    private FoodType[] randomFoods;

    public WorldCupController(WorldCupService worldCupService) {
        this.worldCupService = worldCupService;
    }

    @GetMapping("/randomfood")
    public ResponseEntity<FoodType[]> getRandomFood() {
        randomFoods = worldCupService.getRandomImages();
        return new ResponseEntity<>(randomFoods, HttpStatus.OK);
    }
}
