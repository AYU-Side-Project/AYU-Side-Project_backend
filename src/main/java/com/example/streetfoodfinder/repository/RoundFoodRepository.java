package com.example.streetfoodfinder.repository;

import com.example.streetfoodfinder.domain.entity.RoundFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundFoodRepository extends JpaRepository<RoundFood, Long> {
}
