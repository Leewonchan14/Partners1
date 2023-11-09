package com.example.techeerpartners1.Controller;

import com.example.techeerpartners1.Model.DAO.GENRE;
import com.example.techeerpartners1.Model.DAO.Movie;
import com.example.techeerpartners1.Model.DAO.Review;
import com.example.techeerpartners1.Model.DTO.Request.Review.ReviewCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Review.ReviewAllGetResponseDTO;
import com.example.techeerpartners1.Repository.MovieRepository;
import com.example.techeerpartners1.Repository.ReviewRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ReviewControllerTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MovieRepository movieRepository;


    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MockMvc mockMvc;

    private List<Review> reviewList = new ArrayList<>();


    private ObjectMapper objectMapper = CreateMapper();

    ObjectMapper CreateMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @BeforeEach
    void clear() throws Exception {
        movieRepository.deleteAll();
        for (int i = 1; i <= 3; i++) {
            Movie movie = new Movie();
            movie.setTitle("test" + i);
            movie.setGenre(GENRE.THRILLER);
            movie.setReleaseDate(LocalDateTime.now());
            movie.setEndDate(LocalDateTime.now());
            movie.setIsShowing(true);

            for (int j = 1; j <= 3; j++) {
                Review review = new Review();
                review.setMovie(movie);
                review.setRating(((double) j));
                review.setContent("test" + j);
                movie.addReview(review);
                reviewList.add(review);
            }
            movieRepository.save(movie);
        }
    }

    @Test
    void reviewCreateTest() throws Exception {
        ReviewCreateRequestDTO reqDTO = new ReviewCreateRequestDTO();
        reqDTO.setMovieId(0L);
        reqDTO.setContent("test");
        reqDTO.setRating(5.0);

        mockMvc.perform(
                post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO))
        ).andExpect(status().is(400));

        Movie movie = movieRepository.findAll().stream().findAny().get();

        ReviewCreateRequestDTO reqDTO2 = new ReviewCreateRequestDTO();
        reqDTO2.setMovieId(movie.getId());
        reqDTO2.setRating(5.0);

        mockMvc.perform(post("/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO2))
        ).andExpect(status().is(400));

        ReviewCreateRequestDTO reqDTO3 = new ReviewCreateRequestDTO();
        reqDTO3.setMovieId(movie.getId());
        reqDTO3.setContent("test");
        reqDTO3.setRating(6.0);

        mockMvc.perform(
                post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO3))
        ).andExpect(status().is(400));

        ReviewCreateRequestDTO reqDTO4 = new ReviewCreateRequestDTO();
        reqDTO4.setMovieId(movie.getId());
        reqDTO4.setContent("test");
        reqDTO4.setRating(-1.0);

        String response = mockMvc.perform(
                post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO4))
        ).andExpect(status().is(400)).andReturn().getResponse().getContentAsString();

        Map<String, String> map = objectMapper.readValue(
                response,
                new TypeReference<>() {
                }
        );

        assertTrue(map.containsKey("rating"));
    }

    @Test
    void reviewAllGetErrorTest() throws Exception{
        Movie movie = movieRepository.findAll().stream().findAny().get();

        String response1 = mockMvc.perform(
                get("/review?movieId=0&rating=0&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(400)).andReturn().getResponse().getContentAsString();

        Map<String, String> map1 = objectMapper.readValue(
                response1,
                new TypeReference<>() {
                }
        );

        assertTrue(map1.containsKey("message"));
    }
    void reviewAllGetTest() throws Exception{
        Movie movie = movieRepository.findAll().stream().findAny().get();

        String response2 = mockMvc.perform(
                get("/review?movieId=" + movie.getId() + "&rating=0&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

        List<ReviewAllGetResponseDTO> list = objectMapper.readValue(response2, new TypeReference<>() {});
        assertEquals(movie.getReviews().size(), list.size());
    }

    void reviewAllGetOrderTest() throws Exception {
        Movie movie = movieRepository.findAll().stream().findAny().get();

        String response2 = mockMvc.perform(
                get("/review?movieId=" + movie.getId() + "&rating=0&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

        List<ReviewAllGetResponseDTO> list = objectMapper.readValue(response2, new TypeReference<>() {});

        ReviewAllGetResponseDTO last = list.stream().limit(1).findAny().get();

        Review review = reviewList.stream()
                .filter(r -> r.getMovie().getId().equals(movie.getId()))
                .sorted(Comparator.comparing(Review::getCreatedDate).reversed())
                .limit(1).findAny()
                .get();

        assertEquals(review.getId(), last.getId());
    }

    @Test
    void reviewAllGetRatingTest() throws Exception {
        Movie movie = movieRepository.findAll().stream().findAny().get();

        String response2 = mockMvc.perform(
                get("/review?movieId=" + movie.getId() + "&rating=2.5&page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200)).andReturn().getResponse().getContentAsString();

        List<ReviewAllGetResponseDTO> list = objectMapper.readValue(response2, new TypeReference<>() {});

        boolean b = list.stream().anyMatch(r -> r.getRating() <= 2.5);
        assertFalse(b);
    }


}