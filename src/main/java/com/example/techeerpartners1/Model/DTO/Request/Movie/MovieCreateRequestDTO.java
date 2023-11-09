package com.example.techeerpartners1.Model.DTO.Request.Movie;

import com.example.techeerpartners1.Model.DAO.GENRE;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovieCreateRequestDTO {
    //제목, 장르, 개봉일, 상영 종료일, 상영 중 여부 정보를 포함
    @NotNull(message = "제목은 필수 입력값 입니다.")
    private String title;
    private GENRE genre = GENRE.THRILLER;

    private LocalDateTime releaseDate;
    private LocalDateTime endDate;

    private Boolean isShowing;
}
