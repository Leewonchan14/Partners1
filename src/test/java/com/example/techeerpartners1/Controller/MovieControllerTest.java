package com.example.techeerpartners1.Controller;

import com.example.techeerpartners1.Model.DAO.GENRE;
import com.example.techeerpartners1.Model.DAO.Movie;
import com.example.techeerpartners1.Model.DTO.Request.Movie.MovieCreateRequestDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.MovieAllGetResponseDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.MovieCreateReponseDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.MovieDeleteResponseDTO;
import com.example.techeerpartners1.Model.DTO.Response.Movie.MoviePatchResponseDTO;
import com.example.techeerpartners1.Repository.MovieRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void clear() throws Exception{
        movieRepository.deleteAll();

        for (int i = 1; i <= 3; i++) {
            Movie movie = new Movie();
            movie.setTitle("test" + i);
            movie.setGenre(GENRE.THRILLER);
            movie.setReleaseDate(LocalDateTime.now());
            movie.setEndDate(LocalDateTime.now());
            movie.setIsShowing(true);

            movieRepository.save(movie);
        }
    }

    @Test
    void getAllMovie() throws Exception {
        //given
        // Get "/movie" request parameter page=1, size=10
        MvcResult result = mockMvc.perform(get("/movie?page=0&size=100")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        String response = result.getResponse().getContentAsString();

        //when
        List actualList = objectMapper.readValue(response, List.class);

        List<Movie> expectList = movieRepository.findAll(PageRequest.of(0, 100)).toList();


        //then
        assertEquals(expectList.size(), actualList.size());
    }

    @Test
    void updateDateTest(){
        Movie movie = new Movie();
        movie.setTitle("init");

        movieRepository.save(movie);
        entityManager.flush();

        LocalDateTime modifiedDate = movie.getModifiedDate();

        movie.setTitle("update");
        movieRepository.save(movie);
        entityManager.flush();

        assertEquals("update", movie.getTitle());
        assertNotEquals(modifiedDate, movie.getModifiedDate());
    }

    @Test
    void deleteMovie() throws Exception {
        //given
        Optional<Movie> any = movieRepository.findAll(PageRequest.of(0, 1)).toList().stream().findAny();
        Long id = any.get().getId();


        //when
        String contentBody = mockMvc.perform(delete("/movie/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn().getResponse().getContentAsString();

        MovieDeleteResponseDTO responseDTO = objectMapper.readValue(contentBody, MovieDeleteResponseDTO.class);

        String s = mockMvc.perform(get("/movie?page=0&size=5")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();
        // movie list
        List<MovieAllGetResponseDTO> list = objectMapper.readValue(
                s,
                new TypeReference<>() {}
        );

        boolean b = list.stream().anyMatch(m -> m.getId() == id);

        //then
        assertEquals(id, responseDTO.getId());
        assertFalse(b);
    }

    @Test
    void patchMovie() throws Exception{
        //given
        Movie movie = movieRepository.findAll(PageRequest.of(0, 1)).toList().stream().findAny().get();
        Long id = movie.getId();
        String releaseDate = movie.getReleaseDate().toString();

        LocalDateTime parse = LocalDateTime.parse(releaseDate);

        int dayOfYear = parse.getDayOfYear();
        parse = parse.plusYears(1);

        System.out.println(parse);

        String string = new JSONObject()
                .put("releaseDate", parse)
                .toString();

        System.out.println(string);


        //when
        String content = mockMvc.perform(patch("/movie/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(string))
                .andDo(print()
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andReturn().getResponse().getContentAsString();

        MoviePatchResponseDTO dto = objectMapper.readValue(content, MoviePatchResponseDTO.class);

        //then
        assertEquals(parse ,dto.getReleaseDate());
    }

    @Test
    void getMovie() throws Exception{
        //given
        Movie movie = movieRepository.findAll(PageRequest.of(0, 1)).toList().stream().findAny().get();
        Long id = movie.getId();

        //when
        String content = mockMvc.perform(get("/movie/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andReturn().getResponse().getContentAsString();

        MoviePatchResponseDTO dto = objectMapper.readValue(content, MoviePatchResponseDTO.class);
        //then

        assertEquals(id, dto.getId());
    }
    @Test
    void createMovie() throws Exception {
        //given
        String json = new JSONObject().put("title", "test")
                .put("genre", GENRE.THRILLER)
                .put("releaseDate", LocalDateTime.now())
                .put("endDate", LocalDateTime.now())
                .put("isShowing", true)
                .toString();

        Movie movie = objectMapper.readValue(json, Movie.class);
        //when
        String content = mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MoviePatchResponseDTO dto = objectMapper.readValue(content, MoviePatchResponseDTO.class);

        System.out.println("====================================");
        System.out.println(dto);
        System.out.println(movie);
        System.out.println("====================================");

        //then
        assertEquals(movie.getTitle(), dto.getTitle());
        assertEquals(movie.getGenre(), dto.getGenre());
        assertEquals(movie.getReleaseDate(), dto.getReleaseDate());
        assertEquals(movie.getEndDate(), dto.getEndDate());
        assertEquals(movie.getIsShowing(), dto.getIsShowing());
    }

    @Test
    void genreTest() throws Exception {
        //given
        JSONObject json = new JSONObject();
        json.put("title", "test");
        json.put("genre", "BAD");
        json.put("releaseDate", LocalDateTime.now());
        json.put("endDate", LocalDateTime.now());
        json.put("isShowing", true);
        String s = json.toString();

        //when
        String response = mockMvc.perform(post("/movie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)
        )
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        MovieCreateReponseDTO reponseDTO = objectMapper.readValue(response, MovieCreateReponseDTO.class);

        //then
        System.out.println(response);
    }

    @Test
    void titleTest() throws Exception{
        MovieCreateRequestDTO dto = new MovieCreateRequestDTO();
        dto.setGenre(GENRE.THRILLER);
        dto.setIsShowing(true);
        dto.setReleaseDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now());

        String s = objectMapper.writeValueAsString(dto);


        String response = mockMvc.perform(post("/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s)
                ).andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        HashMap<String, String> map = objectMapper.readValue(response, HashMap.class);

        assertTrue(map.containsKey("title"));
        assertEquals(map.get("title"), "제목은 필수 입력값 입니다.");
    }
}