package com.logistics.base.repository;

import com.logistics.base.domain.model.StorageUnit;
import com.logistics.base.domain.repository.StorageUnitRepository;
import com.logistics.base.repository.entity.StorageUnitEntity;
import com.logistics.base.repository.mapper.StorageUniDbMapper;
import com.logistics.base.repository.panache.StorageUnitPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class StorageUnitRepositoryImpl implements StorageUnitRepository {

    @Inject
    StorageUnitPanacheRepository storageUnitPanacheRepository;
    @Inject
    StorageUniDbMapper storageUniDbMapper;

    @Override
    public Optional<StorageUnit> findByUuid(String uuid) {
        return storageUnitPanacheRepository.findByUuid(uuid)
            .map(storageUniDbMapper::toStorageUnit);
    }

    @Override
    public Set<StorageUnit> findWithProductsQtyByType(String storageType) {
        return storageUnitPanacheRepository.findWithProductsQtyByType(storageType).stream()
            .map(storageUniDbMapper::toStorageUnit)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<StorageUnit> saveAll(Set<StorageUnit> storageUnits) {
        Set<StorageUnitEntity> storageUnitEntities = storageUnits.stream()
            .map(storageUniDbMapper::toStorageUnitEntity)
            .collect(Collectors.toSet());
        storageUnitPanacheRepository.persist(storageUnitEntities);
        return storageUnitEntities.stream()
            .map(storageUniDbMapper::toStorageUnit)
            .collect(Collectors.toSet());
    }

    @Override
    public StorageUnit save(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = storageUniDbMapper.toStorageUnitEntity(storageUnit);
        storageUnitPanacheRepository.persist(storageUnitEntity);
        return storageUniDbMapper.toStorageUnit(storageUnitEntity);
    }
}
