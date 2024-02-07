package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import com.example.streetfoodfinder.domain.form.RestaurantForm;
import com.example.streetfoodfinder.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    public Boolean saveRestaurant(RestaurantForm restaurantForm) {
        Restaurant restaurant = RestaurantForm.from(restaurantForm);
        List<Restaurant> existingRestaurants = restaurantRepository.findAllByRestaurantName(restaurantForm.getRestaurantName());
        for (Restaurant existingRestaurant : existingRestaurants) { //같은 이름의 모든 음식점들 비교
            if (existingRestaurant.equals(restaurant))
                return false;
        }
        restaurantRepository.save(restaurant);
        return true;
    }
}
