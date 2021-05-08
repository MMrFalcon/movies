package com.falcon.movies.web.controller;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.criteria.MovieCriteria;
import com.falcon.movies.service.query.MovieQueryService;
import com.falcon.movies.web.controller.header.CustomHeader;
import com.falcon.movies.web.controller.header.CustomHeaderBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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
        HttpHeaders headers = new CustomHeaderBuilderImpl()
                .setGetMultipleHeader(pageable.getPageNumber(), pageable.getPageSize())
                .setResponseStatus(200).build().getHttpHeaders();
        return ResponseEntity.ok().headers(headers).body(moviesPage.getContent());
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
        log.debug("Request for get MovieDto by id : {}", id);
        Optional<MovieDto> movieDto = movieService.getById(id);
        HttpHeaders headers = new CustomHeaderBuilderImpl()
                .setGetOneHeader().setResponseStatus(200).build().getHttpHeaders();
        return movieDto.map(dto -> ResponseEntity.ok().headers(headers).body(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieDto movieDto) throws URISyntaxException {
        log.debug("Request for save new movie : {}", movieDto);
        if (movieDto.getId() != null) {
            throw new RuntimeException("Id already exists");
        }
        MovieDto savedMovie = movieService.save(movieDto);
        CustomHeader customHeader = new CustomHeaderBuilderImpl().setCreateOrUpdateHeader(1).build();
        return ResponseEntity.created(new URI(customHeader.getReferenceURI())).headers(customHeader.getHttpHeaders())
                .body(savedMovie);
    }

}
