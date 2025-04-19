package com.logistics.base.infrastructure.persistence.mapper;

import com.logistics.base.domain.model.Stock;
import com.logistics.base.infrastructure.persistence.entity.StockEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StockEntityMapper {

  @Inject
  ProductEntityMapper productEntityMapper;

  @Inject
  StorageUnitEntityMapper storageUnitEntityMapper;

  public StockEntity toStockEntity(Stock stock) {
    StockEntity temp = new StockEntity();
    temp.setId(stock.id());

    StockEntity stockEntity = temp.getAttachedEntity();
    stockEntity.setBarcode(stock.barcode());
    stockEntity.setExpirationDate(stock.expirationDate());
    stockEntity.setProduct(productEntityMapper.toProductEntity(stock.product()));
    if (stock.storageUnit() != null) {
      stockEntity.setStorageUnit(storageUnitEntityMapper.toStorageUnitEntity(stock.storageUnit()));
    }
    return stockEntity;
  }

  public Stock toStock(StockEntity stockEntity) {
    var builder = Stock.builder()
      .id(stockEntity.id())
      .barcode(stockEntity.barcode())
      .expirationDate(stockEntity.expirationDate());
    if (stockEntity.product() != null) {
      builder.product(productEntityMapper.toProduct(stockEntity.product()));
    }
    if (stockEntity.storageUnit() != null) {
      builder.storageUnit(storageUnitEntityMapper.toStorageUnit(stockEntity.storageUnit()));
    }
    return builder.build();
  }
}
