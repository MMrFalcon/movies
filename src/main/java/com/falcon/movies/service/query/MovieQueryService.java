package com.falcon.movies.service.query;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Author_;
import com.falcon.movies.entity.Movie;
import com.falcon.movies.entity.Movie_;
import com.falcon.movies.entity.enumeration.MovieType;
import com.falcon.movies.repository.MovieRepository;
import com.falcon.movies.service.criteria.MovieCriteria;
import com.falcon.movies.service.mapper.MovieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MovieQueryService  {

    private final Logger log = LoggerFactory.getLogger(MovieQueryService.class);

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieQueryService(MovieRepository movieRepository, MovieMapper movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    /**
     * Method for filter and pagination information on get requests.
     * @param movieCriteria filter criteria.
     * @param pageable pagination information.
     * @return {@link Page<MovieDto>} of entities.
     */
    @Transactional(readOnly = true)
    public Page<MovieDto> findByCriteria(MovieCriteria movieCriteria, Pageable pageable) {
        log.debug("Request for get MovieDto Page by criteria : {}", movieCriteria);
        Specification<Movie> movieSpecification = createSpecification(movieCriteria);
        return movieRepository.findAll(movieSpecification, pageable).map(movieMapper::toDto);
    }

    protected Specification<Movie> createSpecification(MovieCriteria movieCriteria) {
        Specification<Movie> specification = Specification.where(null);

        if (movieCriteria.getIdEquals() != null) {
            specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(Movie_.id), movieCriteria.getIdEquals()));
        }

        if (movieCriteria.getTitleLike() != null) {
            specification = specification.and(((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.upper(root.get(Movie_.title)),
                            movieCriteria.getTitleLike().toUpperCase() + "%"))
            );
        }

        if (movieCriteria.getAuthorNameLike() != null) {
            specification = specification.and(((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(
                    root.join(Movie_.author, JoinType.LEFT)
                            .get(Author_.name), movieCriteria.getAuthorNameLike() + "%"
            )));
        }

        // Handle not existing enum types
        if (movieCriteria.getMovieTypeIn() != null) {
            List<MovieType> movieTypes = new ArrayList<>();
            for (String movieType : movieCriteria.getMovieTypeIn()) {
                MovieType finalMovieTypeEnum = getConvertedMovieTypeEnum(movieType);
                movieTypes.add(finalMovieTypeEnum);
            }
            specification = specification.and((((root, criteriaQuery, criteriaBuilder) ->
                    root.get(Movie_.movieType).in(movieTypes))));
        }

        // Returns code 400 on not existing enum types
        if (movieCriteria.getMovieTypeEnumIn() != null) {
            specification = specification.and((root, criteriaQuery, criteriaBuilder) ->
                    root.get(Movie_.movieType).in(movieCriteria.getMovieTypeEnumIn()));
        }

        return specification;
    }

    /**
     * Convert String value to enum and handle non existing values.
     * @param movieType String for convert operation.
     * @return {@link MovieType} converted from String or None if not existing type was passed.
     * @throws NullPointerException if movieType is null.
     */
    private MovieType getConvertedMovieTypeEnum(String movieType) {
        MovieType movieTypeEnum;
        try {
            movieTypeEnum =  MovieType.valueOf(movieType);
        } catch (IllegalArgumentException ex) {
            log.error(ex.toString());
            movieTypeEnum = MovieType.NONE;
        }

        return movieTypeEnum;
    }
}
