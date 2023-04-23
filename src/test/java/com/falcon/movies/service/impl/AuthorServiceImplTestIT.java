package com.falcon.movies.service.impl;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.mapper.AuthorMapper;
import com.falcon.movies.service.mapper.AuthorMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class AuthorServiceImplTestIT {

    private final String AUTHOR_NAME = "TEST";
    private final String SECOND_AUTHOR_NAME = "TEST_2";

    @Autowired
    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
        authorService = new AuthorServiceImpl(authorRepository, authorMapper);

        setUpAuthors();
    }

    private void setUpAuthors() {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        authorRepository.save(author);

        Author secondAuthor = new Author();
        secondAuthor.setName(SECOND_AUTHOR_NAME);
        authorRepository.save(secondAuthor);
    }

    @Test
    void saveAll() {
        final String firstNewAuthorName = "IM NEW 1";
        final String secondNewAuthorName = "IM NEW 2";
        List<AuthorDto> authors = new ArrayList<>();

        AuthorDto author = new AuthorDto.Builder(firstNewAuthorName).build();
//        author.setName(firstNewAuthorName);

        AuthorDto author2 = new AuthorDto.Builder(secondNewAuthorName).build();
//        author2.setName(secondNewAuthorName);

        authors.add(author);
        authors.add(author2);

        List<AuthorDto> savedAuthors = authorService.saveAll(authors);

        assertThat(savedAuthors.size()).isEqualTo(2);
        assertThat(savedAuthors.stream().map(AuthorDto::getName)).contains(firstNewAuthorName);
        assertThat(savedAuthors.stream().map(AuthorDto::getName)).contains(secondNewAuthorName);
        assertThat(authorRepository.findAll().stream().map(Author::getName)).contains(firstNewAuthorName);
        assertThat(authorRepository.findAll().stream().map(Author::getName)).contains(secondNewAuthorName);
    }

    @Test
    void save() {
        final String newAuthorName = "This is potato";
        AuthorDto authorDto = new AuthorDto.Builder(newAuthorName).build();
//        authorDto.setName(newAuthorName);

        AuthorDto savedAuthor = authorService.save(authorDto);

        assertThat(savedAuthor.getName()).isEqualTo(newAuthorName);
        assertThat(authorRepository.getOne(savedAuthor.getId()).getName()).isEqualTo(newAuthorName);
    }

    @Test
    void delete() {
        final String deletedAuthorName = "DELETED";

        Author author = new Author();
        author.setName(deletedAuthorName);

        Author savedAuthor = authorRepository.save(author);

        authorService.delete(authorMapper.toDto(savedAuthor));

        assertThat(authorRepository.findAll().stream().map(Author::getName)).doesNotContain(deletedAuthorName);

    }

    @Test
    void deleteById() {
        final String deletedAuthorName = "DELETED";

        Author author = new Author();
        author.setName(deletedAuthorName);

        Author savedAuthor = authorRepository.save(author);

        authorService.deleteById(savedAuthor.getId());

        assertThat(authorRepository.findAll().stream().map(Author::getName)).doesNotContain(deletedAuthorName);
    }

    @Test
    void getAll() {
        final String newAuthorName = "This is potato 2";
        Author author = new Author();
        author.setName(newAuthorName);

        authorRepository.save(author);

        Pageable pageable = PageRequest.of(0, 50);
        Slice<AuthorDto> allAuthors = authorService.getAll(pageable);

        assertThat(allAuthors.getContent().stream().map(AuthorDto::getName)).contains(newAuthorName);
        assertThat(allAuthors.getContent().stream().map(AuthorDto::getName)).contains(AUTHOR_NAME);
        assertThat(allAuthors.getContent().stream().map(AuthorDto::getName)).contains(SECOND_AUTHOR_NAME);
    }

    @Test
    void getById() {
        final String newAuthorName = "This is potato 3";
        Author author = new Author();
        author.setName(newAuthorName);

        Author savedAuthor = authorRepository.save(author);

        Optional<AuthorDto> foundAuthor = authorService.getById(savedAuthor.getId());

        assertThat(foundAuthor.get().getName()).isEqualTo(newAuthorName);
    }
}