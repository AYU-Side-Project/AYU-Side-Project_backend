package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.ReviewForm;
import com.example.streetfoodfinder.service.ReviewService;
import com.example.streetfoodfinder.service.UploadPhotoService;
import com.example.streetfoodfinder.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewservice;
    private final UploadPhotoService uploadPhotoService;
    private final WeatherService weatherLikeTodayService;

    @PostMapping("/create-review") //리뷰 작성
    public ResponseEntity<?> createReview(
            @RequestPart(value = "form", required = false) ReviewForm form,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        if (form != null) { //리뷰를 작성 했다면
            reviewservice.CreateReview(form, image);
            return new ResponseEntity<>("리뷰 작성 완료", HttpStatus.OK);
        }
        else return new ResponseEntity<>("올바른 리뷰를 작성해 주세요", HttpStatus.BAD_REQUEST);


    }

    @PutMapping("/update-review/{reviewId}") //리뷰 수정
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewForm form
    ) {
        reviewservice.updateReview(reviewId, form);
        return new ResponseEntity<>("리뷰 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/delete-review/{reviewId}") //리뷰 삭제
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId
    ) {
        reviewservice.deleteReview(reviewId);
        return new ResponseEntity<>("리뷰 삭제 완료", HttpStatus.OK);
    }

    @GetMapping("/weather") //현재 날씨와 동일한 리뷰의 리스트 반환
    public ResponseEntity<Object> weatherLikeToday(@RequestParam Integer x, @RequestParam Integer y) throws IOException, ParseException {//위도 경도(서울 x=37 y=127)
        return new ResponseEntity<>(weatherLikeTodayService.weatherLikeToday(x, y), HttpStatus.OK);
    }

    @GetMapping("/now") //테스트용 현재 날씨 확인
    public ResponseEntity<Object> lookUpWeather(@RequestParam Integer x, @RequestParam Integer y) throws IOException, ParseException {//위도 경도
        return new ResponseEntity<>(weatherLikeTodayService.lookUpWeather(x, y), HttpStatus.OK);
    }
}
