package com.example.streetfoodfinder.repository;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByRestaurantId(Long restaurantId);
    List<Restaurant> findAllByRestaurantName(String restaurantName);
}
