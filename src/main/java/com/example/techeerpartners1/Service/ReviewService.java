package com.example.techeerpartners1.Service;

import com.example.techeerpartners1.Model.DAO.Movie;
import com.example.techeerpartners1.Model.DAO.Review;
import com.example.techeerpartners1.Model.DTO.Request.Review.ReviewCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Review.ReviewAllGetResponseDTO;
import com.example.techeerpartners1.Model.DTO.Response.Review.ReviewCreateResponseDTO;
import com.example.techeerpartners1.Repository.MovieRepository;
import com.example.techeerpartners1.Repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MovieRepository movieRepository;

    public ReviewCreateResponseDTO createReview(ReviewCreateRequestDTO reviewCreateRequestDTO) {
        Review review = modelMapper.map(reviewCreateRequestDTO, Review.class);

        Long movieId = reviewCreateRequestDTO.getMovieId();

        Optional<Movie> byId = movieRepository.findById(movieId);
        byId.ifPresent(movie -> {
            review.setMovie(movie);
            reviewRepository.save(review);
        });

        if (byId.isEmpty()) {
            throw new RuntimeException("영화가 존재하지 않습니다");
        }

        return modelMapper.map(reviewCreateRequestDTO, ReviewCreateResponseDTO.class);
    }

    public List<ReviewAllGetResponseDTO> getAllReview(Long movieId, Double rating, Integer page,Integer size) {
        Optional<Movie> byId = movieRepository.findById(movieId);
        if (byId.isEmpty()) {
            throw new RuntimeException("영화가 존재하지 않습니다");
        }

        Movie movie = byId.get();
        List<ReviewAllGetResponseDTO> list = reviewRepository
                .findAllByMovieIdAndRatingGreaterThanOrderByCreatedDateDesc(movie.getId(), rating, PageRequest.of(page, size))
                .stream().map(review -> {
                    ReviewAllGetResponseDTO dto = modelMapper.map(review, ReviewAllGetResponseDTO.class);
                    return dto;
                }).collect(Collectors.toList());

        return list;
    }
}
