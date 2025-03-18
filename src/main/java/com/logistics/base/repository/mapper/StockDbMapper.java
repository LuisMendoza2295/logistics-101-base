package com.logistics.base.repository.mapper;

import com.logistics.base.domain.model.Stock;
import com.logistics.base.repository.entity.StockEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StockDbMapper {

    @Inject
    ProductDbMapper productDbMapper;

    @Inject
    StorageUniDbMapper storageUniDbMapper;

    public StockEntity toStockEntity(Stock stock) {
        StockEntity temp = new StockEntity();
        temp.setId(stock.id());

        StockEntity stockEntity = temp.getAttachedEntity();
        stockEntity.setBarcode(stock.barcode());
        stockEntity.setExpirationDate(stock.expirationDate());
        stockEntity.setProduct(productDbMapper.toProductEntity(stock.product()));
        if (stock.storageUnit() != null) {
            stockEntity.setStorageUnit(storageUniDbMapper.toStorageUnitEntity(stock.storageUnit()));
        }
        return stockEntity;
    }

    public Stock toStock(StockEntity stockEntity) {
        var builder = Stock.builder()
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
}
