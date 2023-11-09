package com.example.techeerpartners1.Model.DTO.Request.Movie;

import com.example.techeerpartners1.Model.DAO.GENRE;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class MoviePatchRequestDTO {
    //    제목, 장르, 개봉일, 상영 종료일, 상영 중 여부를 수정합니다
    private String title;
    private GENRE genre;

    private LocalDateTime releaseDate;

    private LocalDateTime endDate;
    private Boolean isShowing;
}
