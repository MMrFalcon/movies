package com.falcon.movies.service;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.service.mapper.AuthorMapper;
import com.falcon.movies.service.mapper.AuthorMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorMapperTest {

    private final String AUTHOR_NAME = "TEST";
    private Author author;
    private AuthorDto authorDto;

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
        setUpAuthors();
    }

    private void setUpAuthors() {
        author = new Author();
        author.setName(AUTHOR_NAME);

        authorDto = AuthorDto.builder(AUTHOR_NAME).build();
    }

    @Test
    public void testEntityFromId() {
        Long id = 123L;
        assertThat(authorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(authorMapper.fromId(null)).isNull();
    }

    @Test
    public void toDtoSlice() {
        List<Author> authors = List.of(author);

        Slice<Author> authorSlice = new SliceImpl<>(authors);
        Slice<AuthorDto> authorDtoSlice = authorMapper.toDto(authorSlice);

        assertThat(authorSlice.getSize()).isEqualTo(1);
        assertThat(authorSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);

        assertThat(authorDtoSlice.getSize()).isEqualTo(1);
        assertThat(authorDtoSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);
    }

    @Test
    public void toEntitySlice() {
        List<AuthorDto> authors = List.of(authorDto);

        Slice<AuthorDto> authorDtoSlice = new SliceImpl<>(authors);
        Slice<Author> authorSlice = authorMapper.toEntity(authorDtoSlice);

        assertThat(authorSlice.getSize()).isEqualTo(1);
        assertThat(authorSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);

        assertThat(authorDtoSlice.getSize()).isEqualTo(1);
        assertThat(authorDtoSlice.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);
    }

    @Test
    public void toDtoPage() {
        List<Author> authors = List.of(author);

        Page<Author> authorPage = new PageImpl<>(authors);
        Page<AuthorDto> authorDtoPage = authorMapper.toDto(authorPage);

        assertThat(authorPage.getSize()).isEqualTo(1);
        assertThat(authorPage.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);

        assertThat(authorDtoPage.getSize()).isEqualTo(1);
        assertThat(authorDtoPage.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);
    }

    @Test
    public void toEntityPage() {
        List<AuthorDto> authors = List.of(authorDto);

        Page<AuthorDto> authorDtoPage = new PageImpl<>(authors);
        Page<Author> authorPage = authorMapper.toEntity(authorDtoPage);

        assertThat(authorPage.getSize()).isEqualTo(1);
        assertThat(authorPage.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);

        assertThat(authorDtoPage.getSize()).isEqualTo(1);
        assertThat(authorDtoPage.getContent().get(0).getName()).isEqualTo(AUTHOR_NAME);
    }

}