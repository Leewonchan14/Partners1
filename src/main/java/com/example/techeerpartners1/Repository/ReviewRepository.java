package com.example.techeerpartners1.Repository;

import com.example.techeerpartners1.Model.DAO.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByMovieIdAndRatingGreaterThanOrderByCreatedDateDesc(Long movieId, Double rating , PageRequest pageRequest);
}
