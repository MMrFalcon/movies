package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.mapper.AuthorMapper;
import com.falcon.movies.service.mapper.EntityMapper;
import com.falcon.movies.service.query.AuthorQueryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MoviesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorControllerTestIT {

    private static final String AUTHOR_NAME = "Falcon123";

    private static final LocalDate BIRTH_DATE = LocalDate.now().minusDays(3);

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

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