package com.falcon.movies.repository;

import com.falcon.movies.entity.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends BaseRepository<Author> {

    Optional<Author> findByName(String name);
}
