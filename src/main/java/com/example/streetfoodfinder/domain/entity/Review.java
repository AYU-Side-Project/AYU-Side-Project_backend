package com.example.streetfoodfinder.domain.entity;
import com.example.streetfoodfinder.domain.form.ReviewForm;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Entity
@Getter
//@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @Column(columnDefinition = "NVARCHAR(30) NOT NULL") //30자 제한
    private String title;

    @Column(columnDefinition = "TEXT NOT NULL")//반드시 기입
    private String content;

    @Column(columnDefinition = "TEXT NOT NULL")//반드시 기입
    private String checklist;

    @Column(columnDefinition = "TEXT NOT NULL")//반드시 기입
    private String weather;

    @NotNull //날짜 관련
    private LocalDateTime createDate;

    private LocalDateTime updateDate; // 수정된 날짜

    public void updateFrom(ReviewForm reviewForm, LocalDateTime updateDate) { //0130 업데이트 값
        this.title = reviewForm.getTitle();
        this.content = reviewForm.getContent();
        this.checklist = reviewForm.getChecklist();
        this.weather = reviewForm.getWeather();
        this.updateDate = updateDate;
    }
}
