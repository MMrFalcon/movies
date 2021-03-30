package com.falcon.movies.service.mapper;

import com.falcon.movies.dto.AuthorDto;
import com.falcon.movies.entity.Author;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDto, Author> {

    default Author fromId(Long id) {
        if (id == null)
            return null;

        Author author = new Author();
        author.setId(id);
        return author;
    }

    default Slice<AuthorDto> toDto(Slice<Author> items) {
        return items.map(this::toDto);
    }

    default Slice<Author> toEntity(Slice<AuthorDto> items) {
        return items.map(this::toEntity);
    }

    default Page<AuthorDto> toDto(Page<Author> items) {
        return items.map(this::toDto);
    }

    default Page<Author> toEntity(Page<AuthorDto> items) {
        return items.map(this::toEntity);
    }
}
