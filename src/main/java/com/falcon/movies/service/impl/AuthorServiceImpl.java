package com.falcon.movies.service.impl;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.AuthorService;
import com.falcon.movies.service.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorServiceImpl extends BaseServiceImpl<Author, AuthorRepository, AuthorDto, AuthorMapper>
        implements AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);


    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        super(authorRepository, authorMapper);
    }

    @Override
    public void seedByRandomData(int dataCount) {
        log.debug("Request for seed by random data, data count: {}", dataCount);
        for (int i = 0 ; i < dataCount ; i++) {
            AuthorDto authorDto = AuthorDto.builder().setName(UUID.randomUUID().toString()).build();
            save(authorDto);
        }
    }
}
