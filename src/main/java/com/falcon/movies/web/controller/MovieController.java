package com.falcon.movies.web.controller;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.criteria.MovieCriteria;
import com.falcon.movies.service.query.MovieQueryService;
import com.falcon.movies.web.exception.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieQueryService movieQueryService;
    private final MovieService movieService;

    public MovieController(MovieQueryService movieQueryService, MovieService movieService) {
        this.movieQueryService = movieQueryService;
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getByCriteria(MovieCriteria movieCriteria, Pageable pageable) {
        log.debug("Rest request for get Movies by criteria : {} ", movieCriteria);
        Page<MovieDto> moviesPage = movieQueryService.findByCriteria(movieCriteria, pageable);
        HttpHeaders headers = ResponseHeaderGenerator.createGetResponseHeaders(moviesPage);
        return ResponseEntity.ok().headers(headers).body(moviesPage.getContent());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        log.debug("Request for get MovieDto by id : {}", id);
        Optional<MovieDto> movieDto = movieService.getById(id);
        return movieDto.map(dto -> ResponseEntity.ok().body(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/movies/seed-by-random-data/{dataCount}")
    public ResponseEntity<Void> seedByRandomData(@PathVariable Integer dataCount) {
        log.debug("Request for seed by random data. Data count {}", dataCount);
        movieService.seedByRandomData(dataCount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieDto> saveMovie(@RequestBody MovieDto movieDto) throws URISyntaxException {
        log.debug("Request for save movie {}", movieDto);
        if (movieDto.getId() != null) {
            throw new InvalidRequestException("A new entity cannot already have and id");
        }
        MovieDto savedMovie = movieService.save(movieDto);
        return ResponseEntity.created(new URI("/api/movies/" + savedMovie.getId())).body(savedMovie);
    }

    @PutMapping("/movies")
    public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieDto) {
        log.debug("Request for update movie {}", movieDto);
        if (movieDto.getId() == null) {
            throw new InvalidRequestException("Missing id.");
        }
        MovieDto savedMovie = movieService.save(movieDto);
        return ResponseEntity.ok(savedMovie);
    }

    @DeleteMapping("/movies/delete-by-id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request for delete movie by id {}", id);
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/movies/delete-all")
    public ResponseEntity<Void> deleteAll() {
        log.debug("Request for delete all movies");
        movieService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
