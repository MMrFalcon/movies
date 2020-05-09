package com.falcon.movies.service.mapper;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class MovieMapperTestIT {

    private final String MOVIE_TITLE = "TEST";
    private final String AUTHOR_NAME = "TESTAUTHOR";

    private MovieMapper movieMapper;

    private MovieDto movieDto;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();
        setUpMovies();
    }

    private void setUpMovies() {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        authorRepository.saveAndFlush(author);

        Movie movie = new Movie();
        movie.setTitle(MOVIE_TITLE);
        movie.setAuthor(author);
        movie.setMovieType(MovieType.COMEDY);
        movieRepository.save(movie);

        movieDto = new MovieDto();
        movieDto.setTitle(MOVIE_TITLE);
        movieDto.setAuthorId(1L);
    }

    @Test
    void toDtoSlice() {
        Pageable pageable = PageRequest.of(0, 1);
        Slice<Movie> movieSlice = movieRepository.findAll(pageable);
        Slice<MovieDto> movieDtoSlice = movieMapper.toDto(movieSlice);

        assertThat(movieSlice.getSize()).isEqualTo(1);
        assertThat(movieSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);
        assertThat(movieSlice.getContent().get(0).getAuthor().getName()).isEqualTo(AUTHOR_NAME);
        assertThat(movieSlice.getContent().get(0).getMovieType()).isEqualTo(MovieType.COMEDY);

        assertThat(movieDtoSlice.getSize()).isEqualTo(1);
        assertThat(movieDtoSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);
        assertThat(movieDtoSlice.getContent().get(0).getAuthorName()).isEqualTo(AUTHOR_NAME);
        assertThat(movieDtoSlice.getContent().get(0).getMovieType()).isEqualTo(MovieType.COMEDY);
    }

    @Test
    public void toEntitySlice() {
        List<MovieDto> movies = new ArrayList<>();
        movies.add(movieDto);

        Slice<MovieDto> movieDtoSlice = new SliceImpl<>(movies);
        Slice<Movie> movieSlice = movieMapper.toEntity(movieDtoSlice);

        assertThat(movieSlice.getSize()).isEqualTo(1);
        assertThat(movieSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);

        assertThat(movieDtoSlice.getSize()).isEqualTo(1);
        assertThat(movieDtoSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);
    }
}