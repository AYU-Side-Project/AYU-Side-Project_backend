package com.example.streetfoodfinder.domain.form;

import com.example.streetfoodfinder.domain.entity.Restaurant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestaurantForm {
    private String restaurantName; //이름
    private String previewImage; //이미지 url
    private Double latitude; //위도
    private Double longitude; //경도

    public static Restaurant from (RestaurantForm from) {
        return Restaurant.builder()
                .restaurantName(from.restaurantName)
                .previewImage(from.previewImage)
                .latitude(from.latitude)
                .longitude(from.longitude)
                .hiddenFood(false)
                .build();
    }
}
