package com.logistics.base.repository;

import com.logistics.base.domain.model.StorageUnit;
import com.logistics.base.domain.repository.StorageUnitRepository;
import com.logistics.base.infrastructure.persistence.entity.StorageUnitEntity;
import com.logistics.base.infrastructure.persistence.mapper.StorageUnitEntityMapper;
import com.logistics.base.infrastructure.persistence.StorageUnitPanacheRepository;
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
  StorageUnitEntityMapper storageUnitEntityMapper;

  @Override
  public Optional<StorageUnit> findByUuid(String uuid) {
    return storageUnitPanacheRepository.findByUuid(uuid)
      .map(storageUnitEntityMapper::toStorageUnit);
  }

  @Override
  public Set<StorageUnit> findWithProductsQtyByType(String storageType) {
    return storageUnitPanacheRepository.findWithProductsQtyByType(storageType).stream()
      .map(storageUnitEntityMapper::toStorageUnit)
      .collect(Collectors.toSet());
  }

  @Override
  public Set<StorageUnit> saveAll(Set<StorageUnit> storageUnits) {
    Set<StorageUnitEntity> storageUnitEntities = storageUnits.stream()
      .map(storageUnitEntityMapper::toStorageUnitEntity)
      .collect(Collectors.toSet());
    storageUnitPanacheRepository.persist(storageUnitEntities);
    return storageUnitEntities.stream()
      .map(storageUnitEntityMapper::toStorageUnit)
      .collect(Collectors.toSet());
  }

  @Override
  public StorageUnit save(StorageUnit storageUnit) {
    StorageUnitEntity storageUnitEntity = storageUnitEntityMapper.toStorageUnitEntity(storageUnit);
    storageUnitPanacheRepository.persist(storageUnitEntity);
    return storageUnitEntityMapper.toStorageUnit(storageUnitEntity);
  }
}
