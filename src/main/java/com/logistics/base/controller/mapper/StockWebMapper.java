package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.StockDTO;
import com.logistics.base.domain.model.Stock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class StockWebMapper {

    @Inject
    ProductWebMapper productWebMapper;
    @Inject
    StorageWebMapper storageWebMapper;

    public StockDTO toStockDTO(Stock stock) {
        return new StockDTO(
            stock.barcode(),
            stock.expirationDate(),
            productWebMapper.toProductDTO(stock.product()),
            Optional.ofNullable(stock.storageUnit())
                .map(storageWebMapper::toStorageUnitDTO)
                .orElse(null));
    }

    public Stock toStock(StockDTO stockDTO) {
        return Stock.builder()
            .barcode(stockDTO.barcode())
            .expirationDate(stockDTO.expirationDate())
            .product(productWebMapper.toProduct(stockDTO.product()))
            .storageUnit(storageWebMapper.toStorageUnit(stockDTO.storageUnit()))
            .build();
    }
}
