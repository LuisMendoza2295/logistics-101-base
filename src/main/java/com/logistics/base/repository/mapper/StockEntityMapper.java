package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Stock;
import com.logistics.base.repository.entity.StockEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StockEntityMapper {

    @Inject
    ProductEntityMapper productEntityMapper;

    @Inject
    StorageUnitEntityMapper storageUnitEntityMapper;

    public StockEntity toStockEntity(Stock stock) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.id = stock.id();
        stockEntity.barcode = stock.barcode();
        stockEntity.expirationDate = stock.expirationDate();
        stockEntity.product = productEntityMapper.toProductEntity(stock.product());
        stockEntity.storageUnit = storageUnitEntityMapper.toStorageUnitEntity(stock.storageUnit());
        return stockEntity;
    }

    public Stock toStock(StockEntity stockEntity) {
        return new Stock(
            stockEntity.id,
            stockEntity.barcode,
            stockEntity.expirationDate,
            productEntityMapper.toProduct(stockEntity.product),
            storageUnitEntityMapper.toStorageUnit(stockEntity.storageUnit)
        );
    }
}
