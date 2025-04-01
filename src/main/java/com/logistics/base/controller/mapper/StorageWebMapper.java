package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.StorageUnitDTO;
import com.logistics.base.domain.model.Dimensions;
import com.logistics.base.domain.model.StorageUnit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class StorageWebMapper {

  @Inject
  ProductWebMapper productWebMapper;

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
      storageUnit.productsWithQty().entrySet().stream()
        .collect(Collectors.toMap(
          entry -> productWebMapper.toProductDTO(entry.getKey()),
          Map.Entry::getValue
        ))
    );
  }

  public StorageUnit toStorageUnit(StorageUnitDTO storageUnitDTO) {
    var builder = StorageUnit.builder()
      .uuid(storageUnitDTO.uuid())
      .storageType(storageUnitDTO.storageType())
      .dimensions(Dimensions.builder()
        .height(storageUnitDTO.height())
        .width(storageUnitDTO.width())
        .length(storageUnitDTO.length())
        .build())
      .weightCapacity(storageUnitDTO.weightCapacity())
      .volumeOccupied(storageUnitDTO.volumeOccupied())
      .weightOccupied(storageUnitDTO.weightOccupied())
      .maxUnits(storageUnitDTO.maxUnits())
      .storageStatus(storageUnitDTO.storageStatus());
    Optional.ofNullable(storageUnitDTO.productsWithQty())
      .ifPresent(productWithQty -> productWithQty
        .forEach((productDTO, qty) -> builder.addProduct(productWebMapper.toProduct(productDTO), qty)));
    return builder.build();
  }
}
