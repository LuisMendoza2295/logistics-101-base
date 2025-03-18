package com.logistics.base.repository;

import com.logistics.base.domain.model.Stock;
import com.logistics.base.domain.repository.StockRepository;
import com.logistics.base.repository.entity.StockEntity;
import com.logistics.base.repository.mapper.StockDbMapper;
import com.logistics.base.repository.panache.StockPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class StockRepositoryImpl implements StockRepository {

    @Inject
    StockPanacheRepository stockPanacheRepository;
    @Inject
    StockDbMapper stockDbMapper;

    @Override
    public Set<Stock> findByBarcodes(Set<String> barcodes) {
        return stockPanacheRepository.findByBarcodes(new ArrayList<>(barcodes)).stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Stock> findByStorageUuidAndBarcodes(String storageUuid, Set<String> barcodes) {
        return stockPanacheRepository.findByStorageUuidAndBarcodes(storageUuid, new ArrayList<>(barcodes)).stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Stock> saveAll(Set<Stock> stocks) {
        Set<StockEntity> stockEntities = stocks.stream()
            .map(stockDbMapper::toStockEntity)
            .collect(Collectors.toSet());
        stockPanacheRepository.persist(stockEntities);
        return stockEntities.stream()
            .map(stockDbMapper::toStock)
            .collect(Collectors.toSet());
    }
}
