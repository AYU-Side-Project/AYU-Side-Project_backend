package com.example.streetfoodfinder.domain.form;
import com.example.streetfoodfinder.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewForm {
    private String title; //제목
    private String content; //내용
    private String checklist; //체크리스트
    private String weather; //날씨

    public static Review reviewfrom (ReviewForm form, LocalDateTime createDate) {
        return Review.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .checklist(form.getChecklist())
                .weather(form.getWeather())
                .createDate(createDate)
                .build();
    }
}
