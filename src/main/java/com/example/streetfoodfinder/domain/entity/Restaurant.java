package com.example.streetfoodfinder.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;
    private String restaurantName; //이름
    private String previewImage; //이미지 url
    private Double latitude; //위도
    private Double longitude; //경도
    private Boolean hiddenFood; //숨겨진푸드

    // 음식점을 저장할 때 이름은 같을 수 있기 때문에 완전히 같은 음식점인지 비교
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false; //음식점 객체가 맞고 null이 아닌지 확인
        Restaurant restaurant = (Restaurant) o;
        return Objects.equals(previewImage, restaurant.previewImage) &&
                Objects.equals(latitude, restaurant.latitude) &&
                Objects.equals(longitude, restaurant.longitude);
        //이미 이름으로 찾은 음식점들이기 때문에 이름 제외 하나라도 다르면 false
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantName, previewImage, latitude, longitude);
    }

}
