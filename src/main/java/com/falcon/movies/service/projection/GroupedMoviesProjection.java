package com.falcon.movies.service.projection;

import org.springframework.beans.factory.annotation.Value;

public record GroupedMoviesProjection(@Value("#{target.maxAuthorId}") Long authorId,
                                      @Value("#{target.moviesCount}") Long moviesCount) {

    public Long getAuthorId() {
        return authorId;
    }


    public Long getMoviesCount() {
        return moviesCount;
    }
}
