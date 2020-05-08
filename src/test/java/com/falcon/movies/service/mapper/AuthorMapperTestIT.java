package com.falcon.movies.service.mapper;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@Transactional
class AuthorMapperTestIT {

    private final String AUTHOR_NAME = "Test";

    private AuthorMapper authorMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
        setUpAuthors();
    }

    private void setUpAuthors() {
        Author author = new Author();
        author.setName(AUTHOR_NAME);
        authorRepository.save(author);
    }

    @Test
    public void toDtoSlice() {
        Pageable pageable = PageRequest.of(0, 1);
        Slice<Author> authorSlice = authorRepository.findAll(pageable);
        Slice<AuthorDto> authorDtoSlice = authorMapper.toDto(authorSlice);

        assertThat(authorSlice.getSize()).isEqualTo(1);
        assertThat(authorSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);

        assertThat(authorDtoSlice.getSize()).isEqualTo(1);
        assertThat(authorDtoSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);
    }

}