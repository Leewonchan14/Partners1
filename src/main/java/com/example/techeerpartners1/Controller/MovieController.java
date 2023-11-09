package com.example.techeerpartners1.Controller;

import com.example.techeerpartners1.Model.DTO.Request.Movie.MovieCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Request.Movie.MoviePatchRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.*;
import com.example.techeerpartners1.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieCreateReponseDTO> createMovie(@RequestBody @Valid MovieCreateRequestDTO movieCreateRequestDTO) {
        return ResponseEntity.ok(movieService.createMovie(movieCreateRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<MovieAllGetResponseDTO>> getAllMovie(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(movieService.getAllMovie(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDeleteResponseDTO> deleteMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.deleteMovie(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MoviePatchResponseDTO> patchMovie(
            @PathVariable Long id,
            @RequestBody MoviePatchRequestDTO moviePatchRequestDTO
    ) {
        return ResponseEntity.ok(movieService.patchMovie(id, moviePatchRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieGetResponseDTO> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }
}

