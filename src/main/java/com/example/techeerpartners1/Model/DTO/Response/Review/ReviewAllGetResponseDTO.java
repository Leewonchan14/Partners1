package com.example.techeerpartners1.Model.DTO.Response.Review;


import com.example.techeerpartners1.Model.DTO.ReviewDTO;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewAllGetResponseDTO extends ReviewDTO {
    private Long movieId;
}
