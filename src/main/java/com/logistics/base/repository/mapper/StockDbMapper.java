package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Stock;
import com.logistics.base.domain.StorageUnit;
import com.logistics.base.repository.entity.StockEntity;
import com.logistics.base.repository.entity.StorageUnitEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StockDbMapper {

    @Inject
    ProductDbMapper productDbMapper;

    @Inject
    StorageUniDbMapper storageUniDbMapper;

    public StockEntity toStockEntity(Stock stock) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setId(stock.id());
        stockEntity.setBarcode(stock.barcode());
        stockEntity.setExpirationDate(stock.expirationDate());
        stockEntity.setProduct(productDbMapper.toProductEntity(stock.product()));
        if (stock.storageUnit() != null) {
            stockEntity.setStorageUnit(storageUniDbMapper.toStorageUnitEntity(stock.storageUnit()));
        }
        return stockEntity.getAttachedEntity();
    }

    public StockEntity toStockEntity(Stock stock, StorageUnitEntity storageUnitEntity) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setId(stock.id());
        stockEntity.setBarcode(stock.barcode());
        stockEntity.setExpirationDate(stock.expirationDate());
        stockEntity.setProduct(productDbMapper.toProductEntity(stock.product()));
        if (stock.storageUnit() != null) {
            stockEntity.setStorageUnit(storageUnitEntity);
        }
        return stockEntity.getAttachedEntity();
    }

    public Stock toStock(StockEntity stockEntity) {
        Stock.Builder builder = Stock.builder()
            .id(stockEntity.id())
            .barcode(stockEntity.barcode())
            .expirationDate(stockEntity.expirationDate());
        if (stockEntity.product() != null) {
            builder.product(productDbMapper.toProduct(stockEntity.product()));
        }
        if (stockEntity.storageUnit() != null) {
            builder.storageUnit(storageUniDbMapper.toStorageUnit(stockEntity.storageUnit()));
        }
        return builder.build();
    }

    public Stock toStock(StockEntity stockEntity, StorageUnit storageUnit) {
        Stock.Builder builder = Stock.builder()
            .id(stockEntity.id())
            .barcode(stockEntity.barcode())
            .expirationDate(stockEntity.expirationDate())
            .storageUnit(storageUnit);
        if (stockEntity.product() != null) {
            builder.product(productDbMapper.toProduct(stockEntity.product()));
        }
        return builder.build();
    }
}
