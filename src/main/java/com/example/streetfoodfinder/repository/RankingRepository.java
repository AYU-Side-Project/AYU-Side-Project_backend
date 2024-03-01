package com.example.streetfoodfinder.repository;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByOrderByLikesDesc();
}