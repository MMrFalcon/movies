package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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

    @Test
    @Transactional
    void createAuthor() throws Exception {
        String authorName = "name";
        LocalDate dateOfBirth = LocalDate.of(2024, 2, 3);
        AuthorDto authorDto = AuthorDto.builder().setName(authorName)
                .setDateOfBirth(dateOfBirth)
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value(authorName))
                .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()));

        Optional<Author> optAuthor = authorRepository.findByName(authorName);
        assertThat(optAuthor.isPresent()).isTrue();
        assertThat(optAuthor.get().getId()).isNotNull();
        assertThat(optAuthor.get().getName()).isEqualTo(authorName);
        assertThat(optAuthor.get().getDateOfBirth()).isEqualTo(dateOfBirth);

    }

    @Test
    @Transactional
    void createAuthorWhenIdWasAdded() throws Exception {
        String authorName = "name";
        LocalDate dateOfBirth = LocalDate.of(2024, 2, 3);
        AuthorDto authorDto = AuthorDto.builder().setName(authorName).setId(author.getId())
                .setDateOfBirth(dateOfBirth)
                .build();

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(authorDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void updateAuthor() throws Exception {
        String authorName = "name";
        LocalDate dateOfBirth = LocalDate.of(2024, 2, 3);
        Long oldAuthorId = author.getId();
        AuthorDto authorDto = AuthorDto.builder().setId(oldAuthorId).setName(authorName)
                .setDateOfBirth(dateOfBirth)
                .build();

        mockMvc.perform(put("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(authorDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(oldAuthorId))
                .andExpect(jsonPath("$.name").value(authorName))
                .andExpect(jsonPath("$.dateOfBirth").value(dateOfBirth.toString()));

        Optional<Author> optAuthor = authorRepository.findById(oldAuthorId);
        assertThat(optAuthor.isPresent()).isTrue();
        assertThat(optAuthor.get().getName()).isEqualTo(authorName);
        assertThat(optAuthor.get().getDateOfBirth()).isEqualTo(dateOfBirth);

    }

    @Test
    @Transactional
    void updateAuthorWhenIdWasNotAdded() throws Exception {
        String authorName = "name";
        LocalDate dateOfBirth = LocalDate.of(2024, 2, 3);
        AuthorDto authorDto = AuthorDto.builder().setId(null).setName(authorName)
                .setDateOfBirth(dateOfBirth)
                .build();

        mockMvc.perform(put("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(authorDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        Long oldAuthorId = author.getId();

        long tableSizeBeforeDelete = authorRepository.count();

        mockMvc.perform(delete("/api/authors/delete-by-id/" + oldAuthorId))
                .andExpect(status().isNoContent());

        assertThat(authorRepository.count()).isEqualTo(tableSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void deleteAll() throws Exception {
        authorService.seedByRandomData(3);
        long tableSizeBeforeDelete = authorRepository.count();
        assertThat(tableSizeBeforeDelete).isEqualTo(4);

        mockMvc.perform(delete("/api/authors/delete-all"))
                .andExpect(status().isNoContent());

        assertThat(authorRepository.count()).isEqualTo(0);
    }

    @Transactional
    @Test
    public void seedAuthors() throws Exception {
        int authorsToAdd = 4;
        long tableSizeBefore = authorRepository.count();

        mockMvc.perform(post("/api/authors/seed-by-random-data/" + authorsToAdd))
                .andExpect(status().isOk());

        assertThat(authorRepository.count()).isEqualTo(tableSizeBefore + authorsToAdd);
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