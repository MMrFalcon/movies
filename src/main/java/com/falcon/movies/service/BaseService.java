package com.falcon.movies.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface BaseService<D> {

    List<D> saveAll(List<D> items);

    D save(D item);

    void delete(D item);

    void deleteById(Long id);

    void deleteAll();

    Slice<D> getAll(Pageable pageable);

    Optional<D> getById(Long id);
}
