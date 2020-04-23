package com.falcon.movies.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MovieMapperTest {

    private MovieMapper movieMapper;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 12L;
        assertThat(movieMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(movieMapper.fromId(null)).isNull();
    }
}