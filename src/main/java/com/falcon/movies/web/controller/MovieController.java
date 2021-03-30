package com.falcon.movies.web.controller;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.service.MovieService;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.criteria.MovieCriteria;
import com.falcon.movies.service.query.MovieQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("/api")
public class MovieController {

    private final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final MovieQueryService movieQueryService;

    public MovieController(MovieQueryService movieQueryService) {
        this.movieQueryService = movieQueryService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getByCriteria(MovieCriteria movieCriteria, Pageable pageable) {
        log.debug("Rest request for get Movies by criteria : {} ", movieCriteria);
        Page<MovieDto> moviesPage = movieQueryService.findByCriteria(movieCriteria, pageable);
        HttpHeaders headers = createGetResponseHeaders(moviesPage);
        return ResponseEntity.ok().headers(headers).body(moviesPage.getContent());
    }

    // TODO add abstract class
    protected HttpHeaders createGetResponseHeaders(Page<MovieDto> page) {
        final int pageNumber = page.getNumber();
        final int pageSize = page.getSize();
        final int nextPage = pageNumber + 1;

        int prevPage = pageNumber - 1;
        if (prevPage < 0) prevPage = 0;

        HttpHeaders headers = new HttpHeaders();
        headers.add("M-Page-Size", String.valueOf(pageSize));
        headers.add("M-Page-Count", String.valueOf(page.getTotalPages()));
        headers.add("M-Page-Number", String.valueOf(pageNumber));
        headers.add("M-Total-Count", String.valueOf(page.getTotalElements()));
        headers.add("M-Next-Page", getUriByPage(nextPage, pageSize));
        headers.add("M-Prev-Page", getUriByPage(prevPage, pageSize));
        return headers;
    }

    protected String getUriByPage(int pageNumber, int pageSize) {
        final UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        return uriComponentsBuilder
                .replaceQueryParam("page", Integer.toString(pageNumber))
                .replaceQueryParam("size", Integer.toString(pageSize))
                .toUriString();
    }
}
