package com.logistics.base.domain.repository;

import com.logistics.base.domain.model.StorageUnit;

import java.util.Optional;
import java.util.Set;

public interface StorageUnitRepository {

  Optional<StorageUnit> findByUuid(String uuid);

  Set<StorageUnit> findWithProductsQtyByType(String storageType);

  Set<StorageUnit> saveAll(Set<StorageUnit> storageUnits);

  StorageUnit save(StorageUnit storageUnit);
}
