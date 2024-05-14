package com.falcon.movies.service.query;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.*; // for static metamodels
import com.falcon.movies.entity.Author_;
import com.falcon.movies.repository.AuthorRepository;
import com.falcon.movies.service.criteria.AuthorCriteria;
import com.falcon.movies.service.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Transactional(readOnly = true)
public class AuthorQueryService implements Serializable {

    private final Logger log = LoggerFactory.getLogger(AuthorQueryService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    public AuthorQueryService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
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
}
