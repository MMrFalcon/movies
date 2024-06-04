package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.impl.AuthorServiceImpl;
import com.falcon.movies.service.mapper.AuthorMapper;
import com.falcon.movies.service.mapper.EntityMapper;
import com.falcon.movies.service.query.AuthorQueryService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MoviesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorControllerTestIT {

    private final Logger log = LoggerFactory.getLogger(AuthorControllerTestIT.class);


    private static final String AUTHOR_NAME = "Falcon123";

    private static final LocalDate BIRTH_DATE = LocalDate.now().minusDays(3);

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieService movieService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    public static Author createEntity(EntityManager entityManager) {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        author.setDateOfBirth(BIRTH_DATE);

        // transaction required
        entityManager.persist(author);
        return author;
    }

    @BeforeEach
    void setUp() {
        author = createEntity(em);
    }


    @AfterEach
    void afterEach() {
        movieRepository.deleteAll();
        authorRepository.deleteAll();
    }


    @Test
    @Transactional
    void checkIdEqualsQuery() throws Exception {
        authorsShouldBeFound("idEquals=" + author.getId());
        authorsShouldNotBeFound("idEquals=791");
    }

    @Test
    @Transactional
    void checkNameLikeQuery() throws Exception {
        authorsShouldBeFound("nameLike=" + AUTHOR_NAME.charAt(0));
        authorsShouldNotBeFound("nameLike=MD");
    }

    @Test
    @Transactional
    void checkDateOfBirthGreaterThanOrEqualQuery() throws Exception {
        authorsShouldBeFound("dateOfBirthGreaterThanOrEqual=" + BIRTH_DATE);
        authorsShouldNotBeFound("dateOfBirthGreaterThanOrEqual=" + LocalDate.now().toString());
    }

    @Test
    @Transactional
    void checkDateOfBirthLessThanOrEqualQuery() throws Exception {
        authorsShouldBeFound("dateOfBirthLessThanOrEqual=" + BIRTH_DATE);
        authorsShouldNotBeFound("dateOfBirthLessThanOrEqual=" + BIRTH_DATE.minusDays(1));
    }

    @Test
    @Transactional
    void getByIdWhenEntityExists() throws Exception {
        mockMvc.perform(get("/api/authors/" + author.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(author.getId().intValue()))
                .andExpect(jsonPath("$.name").value(AUTHOR_NAME))
                .andExpect(jsonPath("$.dateOfBirth").value(author.getDateOfBirth().toString()));
    }

    @Test
    @Transactional
    void getByIdWhenEntityDoesNotExists() throws Exception {
        mockMvc.perform(get("/api/authors/" + 123))
                .andExpect(status().isNotFound());
    }

    /**
     * Before each API query, table data will be truncated to avoid time distortion produced by DB caching mechanism.
     * Time with join:    472809166
     * Time without join: 251907375
     */
    @Test
    @Transactional
    void compareMoviesCountReportsResponseTimeForWithAndWithoutJoinStatement() throws Exception {
        movieRepository.deleteAll();
        authorRepository.deleteAll();
        authorService.seedByRandomData(100);
        movieService.seedByRandomData(1000000);
        TestTransaction.flagForCommit();
        TestTransaction.end();


        TestTransaction.start();
        long startTimeForFindWithoutJoin = System.nanoTime();
        mockMvc.perform(get("/api/authors/reports/movies-count?page=0&size=10"))
                .andExpect(status().isOk());
        long endTimeForFindWithoutJoin = System.nanoTime();
        TestTransaction.end();

        TestTransaction.start();
        movieRepository.deleteAll();
        authorRepository.deleteAll();
        authorService.seedByRandomData(100);
        movieService.seedByRandomData(1000000);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        long startTimeForFindWithJoin = System.nanoTime();
        mockMvc.perform(get("/api/authors/reports/movies-count-with-join?page=0&size=10"))
                .andExpect(status().isOk());
        long endTimeForFindWithJoin = System.nanoTime();
        TestTransaction.end();

        long timeWithoutJoin = endTimeForFindWithoutJoin - startTimeForFindWithoutJoin;
        long timeWithJoin = endTimeForFindWithJoin - startTimeForFindWithJoin;

        log.debug("Time with join: {}, time withoutJoin: {}", timeWithJoin, timeWithoutJoin);
        assertTrue(timeWithoutJoin < timeWithJoin);
    }


    private void authorsShouldBeFound(String filter) throws Exception {
        mockMvc.perform(get("/api/authors?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(author.getName())))
                .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(author.getDateOfBirth().toString())));
    }

    private void authorsShouldNotBeFound(String filter) throws Exception {
        mockMvc.perform(get("/api/authors?" + filter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}