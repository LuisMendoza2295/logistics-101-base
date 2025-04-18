package com.logistics.base.infrastructure.persistence.mapper;

import com.logistics.base.domain.model.Dimensions;
import com.logistics.base.domain.model.StorageUnit;
import com.logistics.base.infrastructure.persistence.entity.StorageUnitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;

@ApplicationScoped
public class StorageUnitEntityMapper {

  @Inject
  ProductEntityMapper productEntityMapper;

  public StorageUnitEntity toStorageUnitEntity(StorageUnit storageUnit) {
    StorageUnitEntity temp = new StorageUnitEntity();
    temp.setId(storageUnit.id());

    StorageUnitEntity storageUnitEntity = temp.getAttachedEntity();
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
    storageUnitEntity.setProductsWithQty(new HashMap<>());
    storageUnit.productsWithQty()
      .forEach(
        (product, qty) -> storageUnitEntity.addProductWithQty(productEntityMapper.toProductEntity(product), qty)
      );
    return storageUnitEntity;
  }

  public StorageUnit toStorageUnit(StorageUnitEntity storageUnitEntity) {
    var builder = StorageUnit.builder()
      .id(storageUnitEntity.id())
      .uuid(storageUnitEntity.uuid())
      .storageType(storageUnitEntity.storageType())
      .dimensions(Dimensions.builder()
        .width(storageUnitEntity.width())
        .height(storageUnitEntity.height())
        .length(storageUnitEntity.length())
        .build())
      .weightCapacity(storageUnitEntity.weightCapacity())
      .volumeOccupied(storageUnitEntity.volumeOccupied())
      .weightOccupied(storageUnitEntity.weightOccupied())
      .maxUnits(storageUnitEntity.maxUnits())
      .storageStatus(storageUnitEntity.storageStatus());
    storageUnitEntity.productsWithQty()
      .forEach(
        (productEntity, qty) -> builder.addProduct(productEntityMapper.toProduct(productEntity), qty)
      );
    return builder.build();
  }
}
