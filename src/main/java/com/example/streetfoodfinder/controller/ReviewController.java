package com.example.streetfoodfinder.controller;

import com.example.streetfoodfinder.domain.form.ReviewForm;
import com.example.streetfoodfinder.service.ReviewService;
import com.example.streetfoodfinder.service.UploadPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewservice;
    private final UploadPhotoService uploadPhotoService;

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
}
