package com.falcon.movies.web.controller;

import com.falcon.movies.service.MovieToWatchService;
import com.falcon.movies.service.projection.MovieToWatchProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieToWatchController {

    private final Logger log = LoggerFactory.getLogger(MovieToWatchController.class);

    private final MovieToWatchService movieToWatchService;

    public MovieToWatchController(MovieToWatchService movieToWatchService) {
        this.movieToWatchService = movieToWatchService;
    }


    @GetMapping("/movies-to-watch")
    public ResponseEntity<List<MovieToWatchProjection>> getMoviesToWatch() {
        log.info("REST request for getMoviesToWatch");
        List<MovieToWatchProjection> moviesToWatch = movieToWatchService.pickMoviesToWatch();
        return ResponseEntity.ok(moviesToWatch);
    }
}
