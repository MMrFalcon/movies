package com.falcon.movies.service.impl;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.mapper.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MovieServiceImpl extends BaseServiceImpl<Movie, MovieRepository, MovieDto, MovieMapper>
    implements MovieService {

    private final Logger log = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final AuthorService authorService;

    public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper, AuthorService authorService) {
        super(movieRepository, movieMapper);
        this.authorService = authorService;
    }

    @Override
    public void seedByRandomData(int dataCount) {
        log.debug("Request for seed by random data, data count: {}", dataCount);
        List<AuthorDto> authors = authorService.getAll(PageRequest.of(0, 20)).getContent();
        int listSize = authors.size();
        for (int i = 0 ; i < dataCount ; i++) {
            int randomAuthorIndex = ThreadLocalRandom.current().nextInt(0, listSize - 1);
            AuthorDto randomAuthor = authors.get(randomAuthorIndex);
            MovieDto movieForSave = MovieDto.builder().setMovieType(MovieType.COMEDY)
                    .setAuthorId(randomAuthor.getId()).setTitle(UUID.randomUUID().toString()).build();
            save(movieForSave);
        }
    }
}
