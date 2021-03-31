package com.falcon.movies.service.impl;

import com.falcon.movies.entity.BaseEntity;
import com.falcon.movies.repository.BaseRepository;
import com.falcon.movies.service.BaseService;
import com.falcon.movies.service.mapper.EntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;


public abstract class BaseServiceImpl<E extends BaseEntity, R extends BaseRepository<E>, D,  M extends EntityMapper<D, E>>
        implements BaseService<D> {

    private final Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    R repository;
    M mapper;

    public BaseServiceImpl(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<D> saveAll(List<D> items) {
        log.debug("Request to save list of entities {}", items);
        List<E> entities = mapper.toEntity(items);
        List<E> savedEntities = repository.saveAll(entities);
        return mapper.toDto(savedEntities);
    }

    @Override
    public D save(D item) {
        log.debug("Request to save entity {}", item);
        E entity = mapper.toEntity(item);
        E savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public void delete(D item) {
        log.debug("Request to delete entity {}", item);
        E entity = mapper.toEntity(item);
        repository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Request to delete entity by id {}", id);
        repository.deleteById(id);
    }

    @Override
    public Slice<D> getAll(Pageable pageable) {
        log.debug("Request to get all entities ");
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Optional<D> getById(Long id) {
        log.debug("Request to get entity by id {}", id);
        return repository.findById(id).map(mapper::toDto);
    }
}
