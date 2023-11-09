package com.example.techeerpartners1.Model.DTO.Response.Movie;

import com.example.techeerpartners1.Model.DTO.MovieDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
//@ToString
public class MoviePatchResponseDTO extends MovieDTO {
    private Long id;
}
