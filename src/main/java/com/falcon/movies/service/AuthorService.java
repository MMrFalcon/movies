package com.falcon.movies.service;

import com.falcon.movies.dto.AuthorDto;

public interface AuthorService extends BaseService<AuthorDto> {
    void seedByRandomData(int dataCount);
}
