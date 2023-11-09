package com.example.techeerpartners1.Model.DTO.Request.Review;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class ReviewCreateRequestDTO {
    @NotNull(message = "영화 id는 필수 입력값 입니다.")
    private Long movieId;

    @NotNull(message = "리뷰 내용은 필수 입력값 입니다.")
    private String content;

    @Max(value = 5, message = "평점은 5점 만점입니다.")
    @Positive(message = "평점은 양수만 가능합니다.")
    private Double rating = 2.5;
}