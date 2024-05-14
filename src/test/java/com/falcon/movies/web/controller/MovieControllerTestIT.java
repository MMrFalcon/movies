package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.enumeration.MovieType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = MoviesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovieControllerTestIT {

    private static final MovieType MOVIE_TYPE = MovieType.COMEDY;

    private static final String MOVIE_TITLE = "TEST3322aa1";

    private static final int MOVIE_TIME = 120;

    private static final LocalDate MOVIE_PRODUCTION_DATE = LocalDate.now().minusDays(845);

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    private Movie movie;

    private Author author;

    public static Movie createEntity(EntityManager entityManager) {
        Movie movie = new Movie();
        movie.setMovieType(MOVIE_TYPE);
        movie.setTitle(MOVIE_TITLE);
        movie.setTime(MOVIE_TIME);
        movie.setProductionDate(MOVIE_PRODUCTION_DATE);
        movie.setAuthor(AuthorControllerTestIT.createEntity(entityManager));

        // Persist Author entity
        entityManager.flush();

        // transaction required
        entityManager.persist(movie);
        return movie;
    }

    @BeforeEach
    void setUp() {
        movie = createEntity(em);
        author = movie.getAuthor();
    }

    @Test
    @Transactional
    void checkIdEqualsQuery() throws Exception {
        moviesShouldBeFound("idEquals=" + movie.getId());
        moviesShouldNotBeFound("idEquals=791");
    }

    @Test
    @Transactional
    void checkTileLikeQuery() throws Exception {
        moviesShouldBeFound("titleLike=" + movie.getTitle().charAt(0));
        moviesShouldNotBeFound("titleLike=long_title");
    }

    @Test
    @Transactional
    void checkAuthorNameLikeQuery() throws Exception {
        moviesShouldBeFound("authorNameLike=" + author.getName().charAt(0));
        moviesShouldNotBeFound("authorNameLike=long_name");
    }

    // tests for movieType filter

    @Test
    @Transactional
    void checkMovieTypeInQuery() throws Exception {
        moviesShouldBeFound("movieTypeIn=" + MOVIE_TYPE.toString());
        moviesShouldNotBeFound("movieTypeIn=NOT_EXISTING_TYPE");
    }

    @Test
    @Transactional
    void checkMovieTypeInForMultipleTypesQuery() throws Exception {
        moviesShouldBeFound("movieTypeIn=" + MovieType.DRAMA.toString() + "," + MOVIE_TYPE.toString());
        moviesShouldNotBeFound("movieTypeIn=BAD_TYPE,BAD_TYPE_2");
    }

    @Test
    @Transactional
    void checkMovieTypeInForMultipleTypesQueryAndBadId() throws Exception {
        moviesShouldNotBeFound("movieTypeIn=" + MovieType.DRAMA.toString() + "," + MOVIE_TYPE.toString() + "&idEquals=791");
    }

    // tests for movieTypeEnum filter

    @Test
    @Transactional
    void checkMovieTypeEnumInQuery() throws Exception {
        moviesShouldBeFound("movieTypeEnumIn=" + MOVIE_TYPE.toString());
        shouldReturnBadRequest("movieTypeEnumIn=NOT_EXISTING_TYPE");
    }

    @Test
    @Transactional
    void checkMovieTypeEnumInForMultipleTypesQuery() throws Exception {
        moviesShouldBeFound("movieTypeEnumIn=" + MovieType.DRAMA.toString() + "," + MOVIE_TYPE.toString());
        moviesShouldNotBeFound("movieTypeEnumIn=" + MovieType.DRAMA.toString());
    }

    @Test
    @Transactional
    void checkMovieTypeEnumInForMultipleTypesQueryAndBadId() throws Exception {
        moviesShouldNotBeFound("movieTypeEnumIn=" + MovieType.DRAMA.toString() + "," + MOVIE_TYPE.toString() + "&idEquals=791");
    }


    @Test
    @Transactional
    void getByIdWhenEntityExists() throws Exception {
        mockMvc.perform(get("/api/movies/" + movie.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
                .andExpect(jsonPath("$.title").value(MOVIE_TITLE))
                .andExpect(jsonPath("$.productionDate").value(MOVIE_PRODUCTION_DATE.toString()))
                .andExpect(jsonPath("$.time").value(MOVIE_TIME))
                .andExpect(jsonPath("$.authorId").value(author.getId().intValue()))
                .andExpect(jsonPath("$.authorName").value(author.getName()));
    }

    @Test
    @Transactional
    void getByIdWhenEntityDoesNotExists() throws Exception {
        mockMvc.perform(get("/api/movies/" + 123))
                .andExpect(status().isNotFound());
    }

    private void moviesShouldBeFound(String filter) throws Exception {
        mockMvc.perform(get("/api/movies?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(MOVIE_TITLE)))
                .andExpect(jsonPath("$.[*].productionDate").value(hasItem(MOVIE_PRODUCTION_DATE.toString())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(MOVIE_TIME)))
                .andExpect(jsonPath("$.[*].authorId").value(hasItem(author.getId().intValue())))
                .andExpect(jsonPath("$.[*].authorName").value(hasItem(author.getName())));
    }

    private void moviesShouldNotBeFound(String filter) throws Exception {
        mockMvc.perform(get("/api/movies?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    private void shouldReturnBadRequest(String filter) throws Exception {
        mockMvc.perform(get("/api/movies?" + filter))
                .andExpect(status().isBadRequest());
    }
}