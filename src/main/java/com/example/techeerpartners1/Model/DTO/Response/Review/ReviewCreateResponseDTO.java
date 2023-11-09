package com.example.techeerpartners1.Model.DTO.Response.Review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewCreateResponseDTO {
    private Long movieId;

    private String content;

    private Double rating;
}
