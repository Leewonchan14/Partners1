package com.example.techeerpartners1.Service;

import com.example.techeerpartners1.Model.DAO.Movie;
import com.example.techeerpartners1.Model.DTO.Request.Movie.MovieCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Request.Movie.MoviePatchRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.*;
import com.example.techeerpartners1.Repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public MovieCreateReponseDTO createMovie(MovieCreateRequestDTO movieCreateRequestDTO){
        ModelMapper modelMapper = new ModelMapper();
        Movie movie = modelMapper.map(movieCreateRequestDTO, Movie.class);
        movieRepository.save(movie);
        return modelMapper.map(movie, MovieCreateReponseDTO.class);
    }

    public List<MovieAllGetResponseDTO> getAllMovie(Integer page, Integer size){
        ModelMapper modelMapper = new ModelMapper();
        PageRequest request = PageRequest.of(page, size);
        List<Movie> pageList = movieRepository.findByIsDeletedFalse(request).toList();
        return pageList.stream().map(m -> modelMapper.map(m, MovieAllGetResponseDTO.class)).collect(Collectors.toList());
    }

    public MovieDeleteResponseDTO deleteMovie(Long id){
        ModelMapper modelMapper = new ModelMapper();
        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isEmpty()){
            throw new RuntimeException("Movie not found");
        }

        Movie findMovie = optional.get();

        findMovie.setIsDeleted(true);

        movieRepository.save(findMovie);

        return modelMapper.map(findMovie, MovieDeleteResponseDTO.class);
    }

    public MoviePatchResponseDTO patchMovie(Long id, MoviePatchRequestDTO moviePatchRequestDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<Movie> optional = movieRepository.findById(id);

        if(optional.isEmpty()){
            throw new RuntimeException("Movie not found");
        }

        Movie movie = optional.get();

        //    제목, 장르, 개봉일, 상영 종료일, 상영 중 여부를 수정합니다
        if(moviePatchRequestDTO.getTitle() != null)
            movie.setTitle(moviePatchRequestDTO.getTitle());
        if(moviePatchRequestDTO.getGenre() != null)
            movie.setGenre(moviePatchRequestDTO.getGenre());
        if(moviePatchRequestDTO.getReleaseDate() != null)
            movie.setReleaseDate(moviePatchRequestDTO.getReleaseDate());
        if(moviePatchRequestDTO.getEndDate() != null)
            movie.setEndDate(moviePatchRequestDTO.getEndDate());
        if(moviePatchRequestDTO.getIsShowing() != null)
            movie.setIsShowing(moviePatchRequestDTO.getIsShowing());

        movieRepository.save(movie);

        return modelMapper.map(movie, MoviePatchResponseDTO.class);
    }

    public MovieGetResponseDTO getMovie(Long id) {
        Optional<Movie> byId = movieRepository.findById(id);
        if (byId.isEmpty()) {
            throw new RuntimeException("Movie not found Id : " + id);
        }

        return objectMapper.convertValue(byId.get(), MovieGetResponseDTO.class);
    }
}
