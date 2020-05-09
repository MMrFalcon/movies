package com.falcon.movies.service.impl;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.mapper.MovieMapper;
import com.falcon.movies.service.mapper.MovieMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class MovieServiceImplTestIT {

    private final String MOVIE_TITLE = "TESTING";
    private final String SECOND_MOVIE_TITLE = "TESTING 2";
    private final MovieType MOVIES_TYPE = MovieType.HORROR;
    private final String AUTHOR_NAME = "Jack";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MovieRepository movieRepository;
    private MovieMapper movieMapper;

    private Author author;
    private Movie firstMovie;
    private Movie secondMovie;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();

        movieService = new MovieServiceImpl(movieRepository, movieMapper);

        setUpMovies();
    }

    private void setUpMovies() {
        author = new Author();
        author.setName(AUTHOR_NAME);

        authorRepository.saveAndFlush(author);

        firstMovie = new Movie();
        firstMovie.setTitle(MOVIE_TITLE);
        firstMovie.setMovieType(MOVIES_TYPE);
        firstMovie.setAuthor(author);
        this.firstMovie = movieRepository.save(firstMovie);

        secondMovie = new Movie();
        secondMovie.setTitle(SECOND_MOVIE_TITLE);
        secondMovie.setMovieType(MOVIES_TYPE);
        secondMovie.setAuthor(author);
        this.secondMovie = movieRepository.save(secondMovie);

    }

    @Test
    void saveAll() {
        final String newMovieTile = "Im new here";
        final String secondNewMovieTitle = "Im new here 2";

        MovieDto movieDto = new MovieDto();
        movieDto.setTitle(newMovieTile);
        movieDto.setMovieType(MOVIES_TYPE);
        movieDto.setAuthorId(author.getId());
        movieDto.setAuthorName(author.getName());

        MovieDto secondMovieDto = new MovieDto();
        secondMovieDto.setTitle(secondNewMovieTitle);
        secondMovieDto.setMovieType(MOVIES_TYPE);
        secondMovieDto.setAuthorId(author.getId());
        secondMovieDto.setAuthorName(author.getName());

        List<MovieDto> moviesForSave = new ArrayList<>();
        moviesForSave.add(movieDto);
        moviesForSave.add(secondMovieDto);

        List<MovieDto> savedMovies = movieService.saveAll(moviesForSave);

        assertThat(savedMovies.size()).isEqualTo(2);
        assertThat(savedMovies.stream().map(MovieDto::getTitle)).contains(newMovieTile);
        assertThat(savedMovies.stream().map(MovieDto::getTitle)).contains(secondNewMovieTitle);
        assertThat(savedMovies.stream().map(MovieDto::getAuthorName)).contains(AUTHOR_NAME);
        assertThat(savedMovies.stream().map(MovieDto::getMovieType)).contains(MOVIES_TYPE);
    }

    @Test
    void save() {
        final String newMovieTile = "Good Movie";

        MovieDto movieDto = new MovieDto();
        movieDto.setTitle(newMovieTile);
        movieDto.setMovieType(MOVIES_TYPE);
        movieDto.setAuthorId(author.getId());
        movieDto.setAuthorName(author.getName());

        MovieDto savedMovie = movieService.save(movieDto);

        assertThat(savedMovie.getTitle()).isEqualTo(newMovieTile);
        assertThat(savedMovie.getAuthorName()).isEqualTo(AUTHOR_NAME);
        assertThat(savedMovie.getAuthorId()).isEqualTo(author.getId());
        assertThat(savedMovie.getMovieType()).isEqualTo(MOVIES_TYPE);
        assertThat(movieRepository.findAll().stream().map(Movie::getTitle)).contains(newMovieTile);
    }

    @Test
    void delete() {

        final String newMovieTile = "Good Movie 2";

        Movie movie = new Movie();
        movie.setTitle(newMovieTile);
        movie.setMovieType(MOVIES_TYPE);
        movie.setAuthor(author);

        Movie savedMovie = movieRepository.save(movie);
        movieService.delete(movieMapper.toDto(savedMovie));

        assertThat(movieRepository.findAll().stream().map(Movie::getTitle)).doesNotContain(newMovieTile);
    }

    @Test
    void deleteById() {

        final String newMovieTile = "Good Movie 3";

        Movie movie = new Movie();
        movie.setTitle(newMovieTile);
        movie.setMovieType(MOVIES_TYPE);
        movie.setAuthor(author);

        Movie savedMovie = movieRepository.save(movie);
        movieService.deleteById(savedMovie.getId());

        assertThat(movieRepository.findAll().stream().map(Movie::getTitle)).doesNotContain(newMovieTile);
    }

    @Test
    void getAll() {
        Pageable pageable = PageRequest.of(0, 50);

        Slice<MovieDto> movieDtoSlice = movieService.getAll(pageable);

        assertThat(movieDtoSlice.getContent().stream().map(MovieDto::getTitle)).contains(MOVIE_TITLE);
        assertThat(movieDtoSlice.getContent().stream().map(MovieDto::getTitle)).contains(SECOND_MOVIE_TITLE);
    }

    @Test
    void getById() {

        MovieDto movieDto = movieService.getById(firstMovie.getId());
        MovieDto movieDto2 = movieService.getById(secondMovie.getId());

        assertThat(movieDto.getMovieType()).isEqualTo(MOVIES_TYPE);
        assertThat(movieDto2.getMovieType()).isEqualTo(MOVIES_TYPE);
        assertThat(movieDto.getTitle()).isEqualTo(MOVIE_TITLE);
        assertThat(movieDto2.getTitle()).isEqualTo(SECOND_MOVIE_TITLE);
    }
}