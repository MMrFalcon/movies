package com.falcon.movies.service;

import com.falcon.movies.service.projection.MovieToWatchProjection;

import java.util.List;

public interface MovieToWatchService {
    List<MovieToWatchProjection> pickMoviesToWatch();
}
