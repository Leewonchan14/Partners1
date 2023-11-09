package com.example.techeerpartners1;

import com.example.techeerpartners1.Model.DAO.GENRE;
import com.example.techeerpartners1.Model.DAO.Movie;
import com.example.techeerpartners1.Model.DAO.Review;
import com.example.techeerpartners1.Repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class TecheerPartners1ApplicationTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void 초기값_설정() {
        movieRepository.deleteAll();
        for (int i = 1; i <= 3; i++) {
            Movie movie = new Movie();
            movie.setTitle("영화" + i);
            movie.setGenre(GENRE.THRILLER);
            movie.setIsShowing(true);
            movie.setReleaseDate(LocalDateTime.now());
            movie.setEndDate(LocalDateTime.now());
            movieRepository.save(movie);


            Review review = new Review();
            review.setMovie(movie);
            review.setRating(((double) i));
            review.setContent("test" + i);

            movie.addReview(review);

            movieRepository.save(movie);
        }
    }
    @Test
    void DB_초기화() {
        movieRepository.deleteAll();
    }
}
