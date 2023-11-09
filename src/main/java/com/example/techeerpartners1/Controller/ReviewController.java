package com.example.techeerpartners1.Controller;

import com.example.techeerpartners1.Model.DTO.Request.Review.ReviewCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Review.ReviewAllGetResponseDTO;
import com.example.techeerpartners1.Model.DTO.Response.Review.ReviewCreateResponseDTO;
import com.example.techeerpartners1.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewCreateResponseDTO> createReview(@RequestBody @Valid ReviewCreateRequestDTO reviewCreateRequestDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewCreateRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ReviewAllGetResponseDTO>> getAllReview(
            @RequestParam @NotNull(message = "영화 id는 필수 입력값 입니다.") Long movieId,
            @RequestParam Double rating,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(reviewService.getAllReview(movieId, rating, page, size));
    }

}
