package com.example.streetfoodfinder.repository;

import com.example.streetfoodfinder.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByTitle(String title);
    Optional<Review> findByContent(String content);
    Optional<Review> findByChecklist(String checklist);
    Optional<Review> findByWeather(String weather);
}