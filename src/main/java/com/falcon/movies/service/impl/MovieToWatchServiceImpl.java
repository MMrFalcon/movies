package com.falcon.movies.service.impl;

import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.MovieToWatchService;
import com.falcon.movies.service.impl.util.RandomMoviePicker;
import com.falcon.movies.service.projection.MovieToWatchProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MovieToWatchServiceImpl implements MovieToWatchService {

    private final Logger log = LoggerFactory.getLogger(MovieToWatchServiceImpl.class);

    private static final int NUMBER_OF_MOVIES_TO_FETCH = 10;

    private final MovieRepository repo;

    public MovieToWatchServiceImpl(MovieRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<MovieToWatchProjection> pickMoviesToWatch() {
        log.debug("Request for pick movies to watch");
        List<MovieToWatchProjection> moviesToWatch = repo.getLatestMovies(NUMBER_OF_MOVIES_TO_FETCH);
        return RandomMoviePicker.from(moviesToWatch).ifEmpty(() -> {
            log.debug("Do some stuff. I'm empty");
            throw new RuntimeException("List is empty.");
        }).ifNonEmptyCapture(capturedMovies -> {
            log.debug("Do some stuff. I'm not empty. The list was copied so we can remove element {}", capturedMovies);
            Iterator<MovieToWatchProjection> iterator = capturedMovies.iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
            log.debug("Captured movies after delete: {}", capturedMovies);
        });
    }
}
