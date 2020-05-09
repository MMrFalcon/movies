package com.falcon.movies.service.mapper;

import com.falcon.movies.entity.BaseEntity;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

    Slice<D> toDto(Slice<E> entitySlice);

    Slice<E> toEntity(Slice<D> dtoSlice);

}
