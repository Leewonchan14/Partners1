package com.example.techeerpartners1.Model.DTO.Response.Movie;

import com.example.techeerpartners1.Model.DTO.MovieDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MovieGetResponseDTO extends MovieDTO {
    private Long id;
    private LocalDateTime modifiedDate;
}
