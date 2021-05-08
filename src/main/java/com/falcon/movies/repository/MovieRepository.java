package com.falcon.movies.repository;

import com.falcon.movies.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie> {
    List<Movie> findByAuthorId(Long authorId);
}
