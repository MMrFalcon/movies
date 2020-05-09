package com.falcon.movies.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface BaseService<D> {

    List<D> saveAll(List<D> items);

    D save(D item);

    void delete(D item);

    void deleteById(Long id);

    Slice<D> getAll(Pageable pageable);

    D getById(Long id);
}
