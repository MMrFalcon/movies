package com.falcon.movies.service;

import com.falcon.movies.dto.MovieDto;

public interface MovieService extends BaseService<MovieDto> {

    void seedByRandomData(int dataCount);
}
