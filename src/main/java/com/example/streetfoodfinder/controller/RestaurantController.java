package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.RestaurantForm;
import com.example.streetfoodfinder.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    @PostMapping("/create")
    public ResponseEntity<Object> createRestaurant(@RequestBody RestaurantForm restaurantForm) {
        if(restaurantService.saveRestaurant(restaurantForm)){
            return new ResponseEntity<>("식당이 생성되었습니다.", HttpStatus.CREATED);
        } else
            return new ResponseEntity<>("이미 존재하는 식당입니다.", HttpStatus.CONFLICT);
    }

}
