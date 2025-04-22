package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MoviesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BigDataTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private AuthorService authorService;

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

        assertTrue(timeWithoutJoin < timeWithJoin);
    }
}
