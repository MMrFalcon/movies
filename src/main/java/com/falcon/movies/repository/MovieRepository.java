package com.falcon.movies.repository;

import com.falcon.movies.entity.Movie;
import com.falcon.movies.service.projection.MovieToWatchProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends BaseRepository<Movie> {

    @Query("""
            SELECT new com.falcon.movies.service.projection.MovieToWatchProjection(
             movie.title, movie.movieType, author.name
            ) from Movie movie
            INNER JOIN Author author on movie.authorId = author.id
            order by movie.productionDate desc
            LIMIT :count
            """)
    List<MovieToWatchProjection> getLatestMovies(int count);
}
