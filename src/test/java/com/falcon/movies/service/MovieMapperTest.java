package com.falcon.movies.service;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.service.mapper.MovieMapper;
import com.falcon.movies.service.mapper.MovieMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MovieMapperTest {

    private final String MOVIE_TITLE = "TEST";

    private Movie movie;
    private MovieDto movieDto;

    private MovieMapper movieMapper;


    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();
        setUpMovies();
    }

    private void setUpMovies() {
        movie = new Movie();
        movie.setTitle(MOVIE_TITLE);

        movieDto = new MovieDto();
        movieDto.setTitle(MOVIE_TITLE);
        movieDto.setAuthorId(1L);
    }

    @Test
    public void testEntityFromId() {
        Long id = 12L;
        assertThat(movieMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(movieMapper.fromId(null)).isNull();
    }

    @Test
    public void toDtoSlice() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);

        Slice<Movie> movieSlice = new SliceImpl<>(movies);
        Slice<MovieDto> movieDtoSlice = movieMapper.toDto(movieSlice);

        assertThat(movieSlice.getSize()).isEqualTo(1);
        assertThat(movieSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);

        assertThat(movieDtoSlice.getSize()).isEqualTo(1);
        assertThat(movieDtoSlice.getContent().get(0).getTitle()).isEqualTo(MOVIE_TITLE);
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