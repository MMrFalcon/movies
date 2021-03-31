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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthorController {

    private final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorQueryService authorQueryService;
    private final AuthorService authorService;

    public AuthorController(AuthorQueryService authorQueryService, AuthorService authorService) {
        this.authorQueryService = authorQueryService;
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDto>> getByCriteria(AuthorCriteria authorCriteria, Pageable pageable) {
        log.debug("Rest request for get Authors by criteria : {} ", authorCriteria);
        Page<AuthorDto> authorsPage = authorQueryService.findByCriteria(authorCriteria, pageable);
        HttpHeaders headers = ResponseHeaderGenerator.createGetResponseHeaders(authorsPage);
        return ResponseEntity.ok().headers(headers).body(authorsPage.getContent());
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
        log.debug("Request for get AuthorDto by id : {}", id);
        Optional<AuthorDto> authorDto = authorService.getById(id);
        return authorDto.map(dto -> ResponseEntity.ok().body(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
