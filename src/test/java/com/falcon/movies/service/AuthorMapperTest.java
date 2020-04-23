package com.falcon.movies.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        authorMapper = new AuthorMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 123L;
        assertThat(authorMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(authorMapper.fromId(null)).isNull();
    }
}