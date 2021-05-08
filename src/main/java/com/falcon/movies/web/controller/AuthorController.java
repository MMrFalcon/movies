package com.falcon.movies.web.controller;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.query.AuthorQueryService;
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
        HttpHeaders headers = new CustomHeaderBuilderImpl()
                .setGetMultipleHeader(pageable.getPageNumber(), pageable.getPageSize())
                .setResponseStatus(200).build().getHttpHeaders();
        return ResponseEntity.ok().headers(headers).body(authorsPage.getContent());
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
        log.debug("Request for get AuthorDto by id : {}", id);
        Optional<AuthorDto> authorDto = authorService.getById(id);
        HttpHeaders headers = new CustomHeaderBuilderImpl()
                .setGetOneHeader().setResponseStatus(200).build().getHttpHeaders();
        return authorDto.map(dto -> ResponseEntity.ok().headers(headers).body(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/authors/{id}/movies/count")
    public ResponseEntity<Integer> getAuthorMovieCount(@PathVariable Long id) {
        log.debug("Request for get movies count for author with id: {}", id);
        Integer moviesCount = authorService.getMoviesCount(id);
        HttpHeaders headers = new CustomHeaderBuilderImpl().setGetOneHeader().build().getHttpHeaders();
        return ResponseEntity.ok().headers(headers).body(moviesCount);
    }

    @PostMapping("/authors")
    public ResponseEntity<AuthorDto> createAuthor(@Valid @RequestBody AuthorDto authorDto) throws URISyntaxException {
        log.debug("Request for create author : {}",  authorDto);
        if (authorDto.getId() != null) {
            throw new RuntimeException("Id already exists");
        }
        AuthorDto savedAuthor = authorService.save(authorDto);
        CustomHeader customHeader = new CustomHeaderBuilderImpl().setCreateOrUpdateHeader(1).build();
        return ResponseEntity.created(new URI(customHeader.getReferenceURI())).headers(customHeader.getHttpHeaders())
                .body(savedAuthor);
    }

    @PutMapping("/authors")
    public ResponseEntity<AuthorDto> updateAuthor(@Valid @RequestBody AuthorDto authorDto) throws URISyntaxException {
        log.debug("Request for update author: {}", authorDto);
        if (authorDto.getId() == null) {
            throw new RuntimeException("Id is empty");
        }
        AuthorDto updatedAuthor = authorService.save(authorDto);
        CustomHeader customHeader = new CustomHeaderBuilderImpl().setCreateOrUpdateHeader(1).build();
        return ResponseEntity.ok().headers(customHeader.getHttpHeaders()).body(updatedAuthor);
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id) {
        log.debug("Request for delete author with id: {}", id);
        authorService.deleteById(id);
        HttpHeaders headers = new CustomHeaderBuilderImpl().setDeleteHeader(1).build().getHttpHeaders();
        return ResponseEntity.noContent().headers(headers).build();
    }
}
