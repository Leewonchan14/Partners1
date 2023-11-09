package com.example.techeerpartners1.Model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {
    private Long id;

    private String content;
    //평점
    private Double rating;

    private LocalDateTime createdDate;
}
