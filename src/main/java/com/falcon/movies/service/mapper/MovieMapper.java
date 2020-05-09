package com.falcon.movies.service.mapper;

import com.falcon.movies.dto.MovieDto;
import com.falcon.movies.entity.Movie;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MovieMapper extends EntityMapper<MovieDto, Movie> {

    @Override
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    MovieDto toDto(Movie entity);

    @Override
    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "authorName", target = "author.name")
    Movie toEntity(MovieDto movieDto);

    default Movie fromId(Long id) {
        if (id == null)
            return null;

        Movie movie = new Movie();
        movie.setId(id);
        return movie;
    }

    default Slice<MovieDto> toDto(Slice<Movie> items) {
        return items.map(this::toDto);
    }

    default Slice<Movie> toEntity(Slice<MovieDto> items) {
        return items.map(this::toEntity);
    }
}
