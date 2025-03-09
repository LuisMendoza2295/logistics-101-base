package com.logistics.base.repository;

import com.logistics.base.repository.entity.StorageUnitEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class StorageUnitRepository implements PanacheRepository<StorageUnitEntity> {

    public Optional<StorageUnitEntity> findByUuid(String uuid) {
        return find("uuid", uuid).firstResultOptional();
    }

    public List<StorageUnitEntity> findByType(String storageType) {
        return list("storageType", storageType);
    }
}
