package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.StorageUnit;
import com.logistics.base.repository.entity.StorageUnitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StorageUniDbMapper {

    @Inject
    StockDbMapper stockDbMapper;

    public StorageUnitEntity toStorageUnitEntity(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = new StorageUnitEntity();
        storageUnitEntity.setId(storageUnit.id());
        storageUnitEntity.setUuid(storageUnit.uuid().toString());
        storageUnitEntity.setStorageType(storageUnit.storageType().name());
        storageUnitEntity.setStorageStatus(storageUnit.storageStatus().name());
        storageUnitEntity.setHeight(storageUnit.dimensions().height());
        storageUnitEntity.setWidth(storageUnit.dimensions().width());
        storageUnitEntity.setLength(storageUnit.dimensions().length());
        storageUnitEntity.setWeightCapacity(storageUnit.weightCapacity());
        storageUnitEntity.setVolumeOccupied(storageUnit.volumeOccupied());
        storageUnitEntity.setWeightOccupied(storageUnit.weightOccupied());
        storageUnitEntity.setMaxUnits(storageUnit.maxUnits());
        // Avoid infinite reference loop and stackoverflow exception
        storageUnit.stocks().stream()
            .map(stock -> stockDbMapper.toStockEntity(stock, storageUnitEntity))
            .forEach(storageUnitEntity::addStock);
        return storageUnitEntity.getAttachedEntity();
    }

    public StorageUnit toStorageUnit(StorageUnitEntity storageUnitEntity) {
        StorageUnit storageUnit = StorageUnit.builder()
            .id(storageUnitEntity.id())
            .uuid(storageUnitEntity.uuid())
            .storageType(storageUnitEntity.storageType())
            .dimensions(new Dimensions(
                storageUnitEntity.width(),
                storageUnitEntity.height(),
                storageUnitEntity.length()
            ))
            .weightCapacity(storageUnitEntity.weightCapacity())
            .volumeOccupied(storageUnitEntity.volumeOccupied())
            .weightOccupied(storageUnitEntity.weightOccupied())
            .maxUnits(storageUnitEntity.maxUnits())
            .storageStatus(storageUnitEntity.storageStatus())
            .build();
        StorageUnit.Builder builder = storageUnit.toBuilder();
        // Avoid infinite reference loop and stackoverflow exception
        storageUnitEntity.stocks().stream()
            .map(stockEntity -> stockDbMapper.toStock(stockEntity, storageUnit))
            .forEach(builder::addStock);
        return builder.build();
    }
}
