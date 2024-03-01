package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import com.example.streetfoodfinder.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRankedRestaurants() {
        return rankingService.getRankedRestaurants();
    }
}