package com.falcon.movies.web.controller;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.query.AuthorQueryService;
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
public class AuthorController {

    private final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorQueryService authorQueryService;

    public AuthorController(AuthorQueryService authorQueryService) {
        this.authorQueryService = authorQueryService;
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> getByCriteria(AuthorCriteria authorCriteria, Pageable pageable) {
        log.debug("Rest request for get Authors by criteria : {} ", authorCriteria);
        Page<AuthorDto> authorsPage = authorQueryService.findByCriteria(authorCriteria, pageable);
        HttpHeaders headers = ResponseHeaderGenerator.createGetResponseHeaders(authorsPage);
        return ResponseEntity.ok().headers(headers).body(authorsPage.getContent());
    }
}
