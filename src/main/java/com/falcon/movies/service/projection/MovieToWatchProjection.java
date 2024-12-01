package com.falcon.movies.service.projection;

import com.falcon.movies.entity.enumeration.MovieType;

public record MovieToWatchProjection(
        String title, MovieType type, String author
) {
}
