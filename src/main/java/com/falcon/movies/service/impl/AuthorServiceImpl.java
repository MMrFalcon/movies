package com.falcon.movies.service.impl;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.mapper.AuthorMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorRepository, AuthorDto, AuthorMapper>
        implements AuthorService {

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        super(authorRepository, authorMapper);
    }
}
