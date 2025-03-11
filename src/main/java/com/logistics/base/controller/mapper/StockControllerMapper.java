package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.StockDTO;
import com.logistics.base.domain.Stock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StockControllerMapper {

    @Inject
    ProductControllerMapper productControllerMapper;
    @Inject
    StorageControllerMapper storageControllerMapper;

    public StockDTO toStockDTO(Stock stock) {
        return new StockDTO(
            stock.barcode(),
            stock.expirationDate(),
            productControllerMapper.toProductDTO(stock.product()),
            storageControllerMapper.toStorageUnitDTO(stock.storageUnit()));
    }
}
