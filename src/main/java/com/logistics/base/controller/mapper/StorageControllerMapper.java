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
    ProductControllerMapper productControllerMapper;

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
            storageUnit.productsWithQty().stream()
                .map(product -> productControllerMapper.toProductDTO(product))
                .collect(Collectors.toSet())
        );
    }

    public StorageUnit toStorageUnit(StorageUnitDTO storageUnitDTO) {
        return new StorageUnit(
            storageUnitDTO.uuid(),
            storageUnitDTO.storageType(),
            new Dimensions(
                storageUnitDTO.width(),
                storageUnitDTO.height(),
                storageUnitDTO.length()),
            storageUnitDTO.weightCapacity(),
            storageUnitDTO.volumeOccupied(),
            storageUnitDTO.weightOccupied(),
            storageUnitDTO.maxUnits(),
            storageUnitDTO.storageStatus());
    }
}
