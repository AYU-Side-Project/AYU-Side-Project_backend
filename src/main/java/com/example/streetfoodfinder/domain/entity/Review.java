package com.example.streetfoodfinder.domain.entity;
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

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateChecklist(String checklist) {
        this.checklist = checklist;
    }

    public void updateWeather(String weather) {
        this.weather = weather;
    }

    public void updateCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public void updateUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
