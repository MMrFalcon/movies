package com.falcon.movies.service.impl;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.mapper.MovieMapper;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl extends BaseServiceImpl<Movie, MovieRepository, MovieDto, MovieMapper>
    implements MovieService {

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        super(movieRepository, movieMapper);
    }
}
