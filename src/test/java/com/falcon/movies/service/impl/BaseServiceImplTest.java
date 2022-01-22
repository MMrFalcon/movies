package com.falcon.movies.service.impl;

import com.falcon.movies.entity.BaseEntity;
import com.falcon.movies.repository.BaseRepository;
import com.falcon.movies.service.mapper.EntityMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The test for base service abstract class. Do not use @SpringBootTest annotation - this is a simple unit testing
 * without application context.
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BaseServiceImplTest {

    @Mock
    private BaseRepository<BaseEntity> baseRepository;

    @Mock
    private EntityMapper<Object, BaseEntity> entityMapper;

    static class ServiceMock extends BaseServiceImpl<BaseEntity, BaseRepository<BaseEntity>, Object, EntityMapper<Object, BaseEntity>> {

        public ServiceMock(BaseRepository<BaseEntity> repository, EntityMapper<Object, BaseEntity> mapper) {
            super(repository, mapper);
        }
    }

    private ServiceMock serviceMock;

    @BeforeEach
    void setUp() {
        serviceMock = new ServiceMock(baseRepository, entityMapper);
    }

    @Test
    void saveAll() {
        // GIVEN
        List<Object> mockedDtoList = new ArrayList<>();
        List<BaseEntity> mockedEntities = new ArrayList<>();

        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);
        BaseEntity mockedMappedEntity = new BaseEntity();
        mockedMappedEntity.setId(333L);

        BaseEntity secondBaseEntityDTO = new BaseEntity();
        secondBaseEntityDTO.setId(321L);
        BaseEntity secondMockedEntity = new BaseEntity();
        secondMockedEntity.setId(444L);

        mockedDtoList.add(baseEntityDTO);
        mockedDtoList.add(secondBaseEntityDTO);

        mockedEntities.add(mockedMappedEntity);
        mockedEntities.add(secondMockedEntity);

        // WHEN
        Mockito.when(entityMapper.toEntity(mockedDtoList)).thenReturn(mockedEntities);
        Mockito.when(baseRepository.saveAll(mockedEntities)).thenReturn(mockedEntities);
        Mockito.when(entityMapper.toDto(mockedEntities)).thenReturn(mockedDtoList);

        serviceMock.saveAll(mockedDtoList);

        // THEN
        Mockito.verify(entityMapper, Mockito.times(1)).toEntity(mockedDtoList);
        Mockito.verify(baseRepository, Mockito.times(1)).saveAll(mockedEntities);
        Mockito.verify(entityMapper, Mockito.times(1)).toDto(mockedEntities);
    }

    @Test
    void save() {
        // GIVEN
        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);
        BaseEntity mockedMappedEntity = new BaseEntity();
        mockedMappedEntity.setId(333L);

        // WHEN
        Mockito.when(entityMapper.toEntity(baseEntityDTO)).thenReturn(mockedMappedEntity);
        Mockito.when(baseRepository.save(mockedMappedEntity)).thenReturn(mockedMappedEntity);
        Mockito.when(entityMapper.toDto(mockedMappedEntity)).thenReturn(baseEntityDTO);

        serviceMock.save(baseEntityDTO);

        // THEN
        Mockito.verify(entityMapper, Mockito.times(1)).toEntity(baseEntityDTO);
        Mockito.verify(baseRepository, Mockito.times(1)).save(mockedMappedEntity);
        Mockito.verify(entityMapper, Mockito.times(1)).toDto(mockedMappedEntity);
    }

    @Test
    void delete() {
        // GIVEN
        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);

        BaseEntity baseEntity = new BaseEntity();
        baseEntity.setId(123L);

        // WHEN
        Mockito.when(entityMapper.toEntity(baseEntityDTO)).thenReturn(baseEntity);
        serviceMock.delete(baseEntityDTO);

        // THEN
        Mockito.verify(entityMapper, Mockito.times(1)).toEntity(baseEntityDTO);
        Mockito.verify(baseRepository, Mockito.times(1)).delete(baseEntity);
    }

    @Test
    void deleteById() {
        // GIVEN
        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);

        // WHEN
        serviceMock.deleteById(baseEntityDTO.getId());

        // THEN
        Mockito.verify(baseRepository, Mockito.times(1)).deleteById(123L);
    }

    @Test
    void getAll() {
        // GIVEN
        Pageable pageable = PageRequest.of(0, 50);

        List<BaseEntity> mockedEntities = new ArrayList<>();

        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);
        BaseEntity mockedMappedEntity = new BaseEntity();
        mockedMappedEntity.setId(333L);

        BaseEntity secondBaseEntityDTO = new BaseEntity();
        secondBaseEntityDTO.setId(321L);
        BaseEntity secondMockedEntity = new BaseEntity();
        secondMockedEntity.setId(444L);

        mockedEntities.add(mockedMappedEntity);
        mockedEntities.add(secondMockedEntity);

        Page<BaseEntity> page = new PageImpl<>(mockedEntities, pageable, 2);

        // WHEN
        Mockito.when(baseRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(entityMapper.toDto(mockedMappedEntity)).thenReturn(baseEntityDTO);
        Mockito.when(entityMapper.toDto(secondMockedEntity)).thenReturn(secondBaseEntityDTO);
        Slice<Object> result = serviceMock.getAll(pageable);

        // THEN
        Assertions.assertEquals(50, result.getSize());
        Mockito.verify(baseRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(entityMapper, Mockito.times(1)).toDto(mockedMappedEntity);
        Mockito.verify(entityMapper, Mockito.times(1)).toDto(secondMockedEntity);
    }

    @Test
    void getById() {
        // GIVEN
        BaseEntity baseEntityDTO = new BaseEntity();
        baseEntityDTO.setId(123L);

        BaseEntity mockedMappedEntity = new BaseEntity();
        mockedMappedEntity.setId(333L);

        // WHEN
        Mockito.when(baseRepository.findById(333L)).thenReturn(Optional.of(mockedMappedEntity));
        Mockito.when(entityMapper.toDto(mockedMappedEntity)).thenReturn(baseEntityDTO);

        Optional<Object> result = serviceMock.getById(333L);

        // THEN
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(baseEntityDTO,  result.get());
        Mockito.verify(baseRepository, Mockito.times(1)).findById(333L);
        Mockito.verify(entityMapper, Mockito.times(1)).toDto(mockedMappedEntity);
    }
}