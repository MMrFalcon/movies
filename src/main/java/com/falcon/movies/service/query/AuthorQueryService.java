package com.falcon.movies.service.query;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.*; // for static metamodels
import com.falcon.movies.entity.Author_;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.mapper.AuthorMapper;
import com.falcon.movies.service.projection.GroupedMoviesProjection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class AuthorQueryService  {

    private final Logger log = LoggerFactory.getLogger(AuthorQueryService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final EntityManager entityManager;

    public AuthorQueryService(AuthorRepository authorRepository, AuthorMapper authorMapper, EntityManager entityManager) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.entityManager = entityManager;
    }

    /**
     * Method for filter and pagination information on get requests.
     * @param authorCriteria filter criteria.
     * @param pageable pagination information.
     * @return {@link Page<AuthorDto>} of entities.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDto> findByCriteria(AuthorCriteria authorCriteria, Pageable pageable) {
        log.debug("Request for get AuthorDto Page by criteria : {}", authorCriteria);
        Specification<Author> specification = createSpecification(authorCriteria);
        return authorRepository.findAll(specification, pageable).map(authorMapper::toDto);
    }

    /**
     * Method for get movies count assigned to each filtered author.
     * @param authorCriteria filter criteria.
     * @param pageable pagination information.
     * @return {@link Page<AuthorDto>} of entities with moviesCount data.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDto> getReportByCriteria(AuthorCriteria authorCriteria, Pageable pageable) {
        log.debug("Request for get report by criteria : {}", authorCriteria);
        List<AuthorDto> authors = findByCriteria(authorCriteria, pageable).toList();
        List<GroupedMoviesProjection> groupedMovies = getGroupedMovies(authors);
        List<AuthorDto> groupedAuthors = seedGroupedAuthors(authors, groupedMovies);
        return new PageImpl<>(groupedAuthors, pageable, groupedAuthors.size());
    }

    /**
     * Method for get movies count assigned to each filtered author.
     * Method executes query with additional join statement for speed comparison with getReportByCriteria.
     * @param authorCriteria filter criteria.
     * @param pageable pagination information.
     * @return {@link Page<AuthorDto>} of entities with moviesCount data.
     */
    @Transactional(readOnly = true)
    public Page<AuthorDto> getReportByCriteriaWithJoin(AuthorCriteria authorCriteria, Pageable pageable) {
        log.debug("Request for get report with additional join by criteria : {}", authorCriteria);
        List<AuthorDto> authors = findByCriteria(authorCriteria, pageable).toList();
        List<GroupedMoviesProjection> groupedMovies = getGroupedMoviesWithAdditionalJoin(authors);
        List<AuthorDto> groupedAuthors = seedGroupedAuthors(authors, groupedMovies);
        return new PageImpl<>(groupedAuthors, pageable, groupedAuthors.size());
    }

    private static List<AuthorDto> seedGroupedAuthors(List<AuthorDto> authors, List<GroupedMoviesProjection> groupedMovies) {
        return authors.stream().map(author -> groupedMovies.stream()
                .filter(groupedMovie -> groupedMovie.getAuthorId().equals(author.getId())).findFirst()
                .map(foundGroupedMovie -> AuthorDto.copy(author).setMoviesCount(foundGroupedMovie.getMoviesCount()).build())
                .orElse(AuthorDto.builder().build())).toList();
    }

    protected Specification<Author> createSpecification(AuthorCriteria authorCriteria) {
        Specification<Author>specification =Specification.where(null);

        if (authorCriteria.getNameLike() != null) {
            specification = specification.and(
                    (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.upper(root.get(Author_.name)), (authorCriteria.getNameLike().toUpperCase() + "%")
                    )
            );
        }

        if (authorCriteria.getIdEquals() != null) {
            specification = specification.and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(Author_.id), authorCriteria.getIdEquals())
            );
        }

        if (authorCriteria.getDateOfBirthGreaterThanOrEqual() != null) {
            specification = specification.and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get(Author_.dateOfBirth),
                                    authorCriteria.getDateOfBirthGreaterThanOrEqual()
                            )
            );
        }

        if (authorCriteria.getDateOfBirthLessThanOrEqual() != null) {
            specification = specification.and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get(Author_.dateOfBirth),
                                    authorCriteria.getDateOfBirthLessThanOrEqual()
                            )
            );
        }

        return specification;
    }

    private List<GroupedMoviesProjection> getGroupedMovies(List<AuthorDto> authorsFromPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GroupedMoviesProjection> movieQuery = criteriaBuilder.createQuery(GroupedMoviesProjection.class);
        final Root<Movie> movieRoot = movieQuery.from(Movie.class);
        final Path<Long> authorFromMoviePath = movieRoot.get(Movie_.authorId);

        CompoundSelection<GroupedMoviesProjection> selectClause = criteriaBuilder.construct(GroupedMoviesProjection.class,
                criteriaBuilder.max(authorFromMoviePath).alias("maxAuthorId"),
                criteriaBuilder.count(authorFromMoviePath).alias("moviesCount"));

        movieQuery.select(selectClause)
        .where(authorFromMoviePath.in(authorsFromPage.stream().map(AuthorDto::getId).toList()))
        .groupBy(authorFromMoviePath);

        TypedQuery<GroupedMoviesProjection> typedQuery = entityManager.createQuery(movieQuery);

        return typedQuery.getResultList();
    }

    private List<GroupedMoviesProjection> getGroupedMoviesWithAdditionalJoin(List<AuthorDto> authorsFromPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GroupedMoviesProjection> movieQuery = criteriaBuilder.createQuery(GroupedMoviesProjection.class);
        final Root<Movie> movieRoot = movieQuery.from(Movie.class);
        // not needed join replaced by @Column(name = "author_id", insertable = false, updatable = false) in Movie.java
        final Path<Long> authorFromMoviePath = movieRoot.join(Movie_.author).get(Author_.id);

        CompoundSelection<GroupedMoviesProjection> selectClause = criteriaBuilder.construct(GroupedMoviesProjection.class,
                criteriaBuilder.max(authorFromMoviePath).alias("maxAuthorId"),
                criteriaBuilder.count(authorFromMoviePath).alias("moviesCount"));

        movieQuery.select(selectClause)
                .where(authorFromMoviePath.in(authorsFromPage.stream().map(AuthorDto::getId).toList()))
                .groupBy(authorFromMoviePath);

        TypedQuery<GroupedMoviesProjection> typedQuery = entityManager.createQuery(movieQuery);

        return typedQuery.getResultList();
    }
}
