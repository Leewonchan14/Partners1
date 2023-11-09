package com.example.techeerpartners1.Model.DTO;

import com.example.techeerpartners1.Model.DAO.GENRE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
@ToString
public class MovieDTO {
    private String title;
    private GENRE genre;
    private LocalDateTime releaseDate;
    private LocalDateTime endDate;
    private LocalDateTime modifiedDate;
    private Boolean isShowing;
}
