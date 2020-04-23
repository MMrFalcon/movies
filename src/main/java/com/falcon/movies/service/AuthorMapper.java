package com.falcon.movies.service;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDto, Author> {

    default Author fromId(Long id) {
        if (id == null)
            return null;

        Author author = new Author();
        author.setId(id);
        return author;
    }
}
