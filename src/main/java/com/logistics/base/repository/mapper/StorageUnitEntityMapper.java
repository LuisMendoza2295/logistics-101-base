package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.StorageUnit;
import com.logistics.base.repository.entity.StorageUnitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

@ApplicationScoped
public class StorageUnitEntityMapper {

    @Inject
    StockEntityMapper stockEntityMapper;

    public StorageUnitEntity toStorageUnitEntity(StorageUnit storageUnit) {
        StorageUnitEntity storageUnitEntity = new StorageUnitEntity();
        storageUnitEntity.id = storageUnit.id();
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
        storageUnitEntity.stocks = storageUnit.stocks().stream()
            .map(stock -> stockEntityMapper.toStockEntity(stock))
            .collect(Collectors.toSet());
        return storageUnitEntity;
    }

    public StorageUnit toStorageUnit(StorageUnitEntity storageUnitEntity) {
        StorageUnit.Builder builder = StorageUnit.builder()
            .id(storageUnitEntity.id)
            .uuid(storageUnitEntity.uuid)
            .storageType(storageUnitEntity.storageType)
            .dimensions(new Dimensions(
                storageUnitEntity.width,
                storageUnitEntity.height,
                storageUnitEntity.length
            ))
            .weightCapacity(storageUnitEntity.weightCapacity)
            .volumeOccupied(storageUnitEntity.volumeOccupied)
            .weightOccupied(storageUnitEntity.weightOccupied)
            .maxUnits(storageUnitEntity.maxUnits)
            .storageStatus(storageUnitEntity.storageStatus);
        storageUnitEntity.stocks.stream()
            .map(stockEntity -> stockEntityMapper.toStock(stockEntity))
            .forEach(builder::addStock);
        return builder.build();
    }
}
