package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import com.example.streetfoodfinder.repository.RankingRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;

    public RankingService(RankingRepository rankingRepository, RedisTemplate<String, String> redisTemplate) {
        this.rankingRepository = rankingRepository;
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public List<Restaurant> getRankedRestaurants() {
        String sortedSetKey = "restaurantLikes";

        // Redis에서 좋아요 수를 가져온 후 Sorted Set에 추가
        List<Restaurant> restaurants = rankingRepository.findAll();
        restaurants.forEach(restaurant -> {
            zSetOperations.add(sortedSetKey, String.valueOf(restaurant.getRestaurantId()), (double)restaurant.getLikes());
        });

        // 좋아요 수가 많은 순으로 Sorted Set에서 레스토랑 아이디 목록을 가져옴
        Set<String> rankedRestaurantIds = zSetOperations.reverseRange(sortedSetKey, 0, -1);

        // 아이디 목록을 이용해서 레스토랑 정보를 데이터베이스에서 가져옴
        List<Restaurant> rankedRestaurants = rankedRestaurantIds.stream()
                .map(id -> rankingRepository.findById(Long.parseLong(id)).orElse(null))
                .collect(Collectors.toList());

        // Redis에 저장된 Sorted Set 삭제 (필요에 따라 주석 처리)
        // redisTemplate.delete(sortedSetKey);

        return rankedRestaurants;
    }
}
