package com.falcon.movies.service.impl;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorRepository, AuthorDto, AuthorMapper>
        implements AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final MovieRepository movieRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
                             MovieRepository movieRepository) {
        super(authorRepository, authorMapper);
        this.movieRepository = movieRepository;
    }

    @Override
    public int getMoviesCount(Long authorId) {
        log.debug("Request for get movies count by author id {}", authorId);
        List<Movie> movieList = movieRepository.findByAuthorId(authorId);
        return movieList.size();
    }
}
