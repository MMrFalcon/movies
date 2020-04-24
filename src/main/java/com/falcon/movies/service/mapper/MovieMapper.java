package com.falcon.movies.service.mapper;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface MovieMapper extends EntityMapper<MovieDto, Movie> {

    @Override
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    MovieDto toDto(Movie entity);

    @Override
    @Mapping(source = "authorId", target = "author")
    Movie toEntity(MovieDto movieDto);

    default Movie fromId(Long id) {
        if (id == null)
            return null;

        Movie movie = new Movie();
        movie.setId(id);
        return movie;
    }
}
