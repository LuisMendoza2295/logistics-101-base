package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.StorageUnitDTO;
import com.logistics.base.domain.Dimensions;
import com.logistics.base.domain.StorageUnit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

@ApplicationScoped
public class StorageControllerMapper {

    @Inject
    StockControllerMapper stockControllerMapper;

    public StorageUnitDTO toStorageUnitDTO(StorageUnit storageUnit) {
        return new StorageUnitDTO(
            storageUnit.uuid().toString(),
            storageUnit.storageType().name(),
            storageUnit.dimensions().height(),
            storageUnit.dimensions().width(),
            storageUnit.dimensions().length(),
            storageUnit.weightCapacity(),
            storageUnit.volumeOccupied(),
            storageUnit.weightOccupied(),
            storageUnit.maxUnits(),
            storageUnit.storageStatus().name(),
            storageUnit.stocks().stream()
                .map(stock -> stockControllerMapper.toStockDTO(stock))
                .collect(Collectors.toSet())
        );
    }

    public StorageUnit toStorageUnit(StorageUnitDTO storageUnitDTO) {
        return StorageUnit.builder()
            .uuid(storageUnitDTO.uuid())
            .storageType(storageUnitDTO.storageType())
            .dimensions(new Dimensions(
                storageUnitDTO.width(),
                storageUnitDTO.height(),
                storageUnitDTO.length()))
            .weightCapacity(storageUnitDTO.weightCapacity())
            .volumeOccupied(storageUnitDTO.volumeOccupied())
            .weightOccupied(storageUnitDTO.weightOccupied())
            .maxUnits(storageUnitDTO.maxUnits())
            .storageStatus(storageUnitDTO.storageStatus())
            .build();
    }
}
