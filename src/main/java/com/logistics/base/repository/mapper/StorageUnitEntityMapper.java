package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.StorageStatus;
import com.logistics.base.domain.StorageType;
import com.logistics.base.domain.StorageUnit;
import com.logistics.base.repository.entity.StorageUnitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class StorageUnitEntityMapper {

    @Inject
    ProductEntityMapper productEntityMapper;

    public StorageUnitEntity toStorageUnitEntity(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = new StorageUnitEntity();
        storageUnitEntity.uuid = storageUnit.uuid().toString();
        storageUnitEntity.storageType = storageUnit.storageType().name();
        storageUnitEntity.storageStatus = storageUnit.storageStatus().name();
        storageUnitEntity.height = storageUnit.dimensions().height();
        storageUnitEntity.width = storageUnit.dimensions().width();
        storageUnitEntity.length = storageUnit.dimensions().length();
        storageUnitEntity.weightCapacity = storageUnit.weightCapacity();
        storageUnitEntity.volumeOccupied = storageUnit.volumeOccupied();
        storageUnitEntity.weightOccupied = storageUnit.weightOccupied();
        storageUnitEntity.maxUnits = storageUnit.maxUnits();
        storageUnitEntity.productsWithQty = storageUnit.productsWithQty().entrySet().stream()
            .collect(Collectors.toMap(
                productQtyEntry -> productEntityMapper.toProductEntity(productQtyEntry.getKey()),
                Map.Entry::getValue
            ));
        return storageUnitEntity;
    }

    public StorageUnit toStorageUnit(StorageUnitEntity storageUnitEntity) {
        return new StorageUnit(
            UUID.fromString(storageUnitEntity.uuid),
            StorageType.valueOf(storageUnitEntity.storageType),
            new Dimensions(
                storageUnitEntity.width,
                storageUnitEntity.height,
                storageUnitEntity.length
            ),
            storageUnitEntity.weightCapacity,
            storageUnitEntity.volumeOccupied,
            storageUnitEntity.weightOccupied,
            storageUnitEntity.maxUnits,
            StorageStatus.valueOf(storageUnitEntity.storageStatus),
            storageUnitEntity.productsWithQty.entrySet().stream()
                .collect(Collectors.toMap(
                    productQtyEntry -> productEntityMapper.toProduct(productQtyEntry.getKey()),
                    Map.Entry::getValue
                ))
        );
    }
}
