package com.example.techeerpartners1.Model.DAO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Movie {
    //영화 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;
    //제목
    private String title = "default";
    //장르
    private GENRE genre = GENRE.THRILLER;
    //개봉일
    private LocalDateTime releaseDate = LocalDateTime.now();
    //상영 종료일
    private LocalDateTime endDate = LocalDateTime.now();
    //등록 날짜
    @CreatedDate
    private LocalDateTime createdDate;
    //수정 날짜
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    //상영 중 여부
    private Boolean isShowing = true;
    //삭제 여부
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        this.reviews.add(review);
        review.setMovie(this);
    }
}
