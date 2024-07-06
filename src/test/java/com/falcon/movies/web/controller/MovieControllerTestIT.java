package com.falcon.movies.web.controller;

import com.falcon.movies.MoviesApplication;
import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.Movie_;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.util.TestUtil;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Nullable
    public static Movie findByTitle(EntityManager em, String titleStartsWith) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
            Root<Movie> root = criteriaQuery.from(Movie.class);
            Predicate likeFilter = criteriaBuilder.like(root.get(Movie_.title), titleStartsWith + "%");
            CriteriaQuery<Movie> queryWithFilter = criteriaQuery.select(root).where(likeFilter);
            TypedQuery<Movie> typedQuery = em.createQuery(queryWithFilter);

            return typedQuery.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    public static long countMovies(EntityManager em) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Movie> root = criteriaQuery.from(Movie.class);
        CriteriaQuery<Long> queryWithFilter = criteriaQuery.select(criteriaBuilder.count(root));
        TypedQuery<Long> typedQuery = em.createQuery(queryWithFilter);

        return typedQuery.getSingleResult();
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

    @Test
    @Transactional
    void createMovie() throws Exception {
        String movieTitle = "title";
        int movieTime = 123;
        LocalDate productionDate = LocalDate.of(1998, 3, 23);
        LocalDate now = LocalDate.now();
        Long authorId = author.getId();
        MovieDto movieDto = MovieDto.builder().setTitle(movieTitle).setProductionDate(productionDate)
                .setCreationDate(now).setUpdateDate(now).setTime(movieTime).setMovieType(MovieType.COMEDY)
                .setAuthorId(authorId).build();



        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(movieDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.title").value(movieTitle))
                .andExpect(jsonPath("$.movieType").value(MovieType.COMEDY.toString()))
                .andExpect(jsonPath("$.productionDate").value(productionDate.toString()))
                .andExpect(jsonPath("$.creationDate").value(now.toString()))
                .andExpect(jsonPath("$.updateDate").value(now.toString()))
                .andExpect(jsonPath("$.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.time").value(String.valueOf(movieTime)));

        Movie foundMovie = findByTitle(em, movieTitle);
        assertThat(foundMovie).isNotNull();
        assertThat(foundMovie.getTitle()).isEqualTo(movieTitle);
        assertThat(foundMovie.getMovieType()).isEqualTo(MovieType.COMEDY);
        assertThat(foundMovie.getProductionDate()).isEqualTo(productionDate);
        assertThat(foundMovie.getCreationDate()).isEqualTo(now);
        assertThat(foundMovie.getUpdateDate()).isEqualTo(now);
        assertThat(foundMovie.getAuthorId()).isEqualTo(authorId);
        assertThat(foundMovie.getTime()).isEqualTo(movieTime);
    }

    @Test
    @Transactional
    void createMovieWhenIdWasAdded() throws Exception {
        String movieTitle = "title";
        int movieTime = 123;
        LocalDate productionDate = LocalDate.of(1998, 3, 23);
        LocalDate now = LocalDate.now();
        Long authorId = author.getId();
        MovieDto movieDto = MovieDto.builder().setId(movie.getId()).setTitle(movieTitle).setProductionDate(productionDate)
                .setCreationDate(now).setUpdateDate(now).setTime(movieTime).setMovieType(MovieType.COMEDY)
                .setAuthorId(authorId).build();



        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(movieDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    void updateMovie() throws Exception {
        String movieTitle = "title";
        int movieTime = 123;
        LocalDate productionDate = LocalDate.of(1998, 3, 23);
        LocalDate now = LocalDate.now();
        Long authorId = author.getId();
        String oldMovieTitle = movie.getTitle();
        MovieDto movieDto = MovieDto.builder().setId(movie.getId()).setTitle(movieTitle)
                .setProductionDate(productionDate).setCreationDate(now).setUpdateDate(now).setTime(movieTime)
                .setMovieType(MovieType.COMEDY).setAuthorId(authorId).build();



        mockMvc.perform(put("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(movieDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.title").value(movieTitle))
                .andExpect(jsonPath("$.movieType").value(MovieType.COMEDY.toString()))
                .andExpect(jsonPath("$.productionDate").value(productionDate.toString()))
                .andExpect(jsonPath("$.creationDate").value(now.toString()))
                .andExpect(jsonPath("$.updateDate").value(now.toString()))
                .andExpect(jsonPath("$.authorId").value(authorId.toString()))
                .andExpect(jsonPath("$.time").value(String.valueOf(movieTime)));

        Movie foundMovie = findByTitle(em, movieTitle);
        assertThat(foundMovie).isNotNull();
        assertThat(foundMovie.getTitle()).isEqualTo(movieTitle);
        assertThat(foundMovie.getMovieType()).isEqualTo(MovieType.COMEDY);
        assertThat(foundMovie.getProductionDate()).isEqualTo(productionDate);
        assertThat(foundMovie.getCreationDate()).isEqualTo(now);
        assertThat(foundMovie.getUpdateDate()).isEqualTo(now);
        assertThat(foundMovie.getAuthorId()).isEqualTo(authorId);
        assertThat(foundMovie.getTime()).isEqualTo(movieTime);

        Movie oldMovieByTitle = findByTitle(em, oldMovieTitle);
        assertThat(oldMovieByTitle).isNull();
    }

    @Test
    @Transactional
    void updateMovieWhenIdWasNotAdded() throws Exception {
        String movieTitle = "title";
        int movieTime = 123;
        LocalDate productionDate = LocalDate.of(1998, 3, 23);
        LocalDate now = LocalDate.now();
        Long authorId = author.getId();
        String oldMovieTitle = movie.getTitle();
        MovieDto movieDto = MovieDto.builder().setId(null).setTitle(movieTitle)
                .setProductionDate(productionDate).setCreationDate(now).setUpdateDate(now).setTime(movieTime)
                .setMovieType(MovieType.COMEDY).setAuthorId(authorId).build();



        mockMvc.perform(put("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(movieDto)))
                .andExpect(status().isBadRequest());

        Movie oldMovieByTitle = findByTitle(em, oldMovieTitle);
        assertThat(oldMovieByTitle).isNotNull();

        Movie updatedMovie = findByTitle(em, movieTitle);
        assertThat(updatedMovie).isNull();
    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        Long oldMovieId = movie.getId();

        long tableSizeBeforeDelete = countMovies(em);

        mockMvc.perform(delete("/api/movies/delete-by-id/" + oldMovieId))
                .andExpect(status().isNoContent());

        assertThat(countMovies(em)).isEqualTo(tableSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void deleteAll() throws Exception {
        Movie secondMovie = new Movie();
        Movie thirdMovie = new Movie();
        BeanUtils.copyProperties(movie, secondMovie);
        BeanUtils.copyProperties(movie, thirdMovie);
        secondMovie.setId(null);
        secondMovie.setTitle("SecondMovieTitle");
        thirdMovie.setId(null);
        thirdMovie.setTitle("ThirdMovieTitle");
        em.persist(secondMovie);
        em.persist(thirdMovie);
        em.flush();

        long tableSizeBeforeDelete = countMovies(em);
        assertThat(tableSizeBeforeDelete).isEqualTo(3);

        mockMvc.perform(delete("/api/movies/delete-all"))
                .andExpect(status().isNoContent());

        assertThat(countMovies(em)).isEqualTo(0);
    }

    @Transactional
    @Test
    public void seedMovies() throws Exception {
        int moviesCountToAdd = 4;
        long tableSizeBefore = countMovies(em);

        mockMvc.perform(post("/api/movies/seed-by-random-data/" + moviesCountToAdd))
                .andExpect(status().isOk());

        assertThat(countMovies(em)).isEqualTo(tableSizeBefore + moviesCountToAdd);
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