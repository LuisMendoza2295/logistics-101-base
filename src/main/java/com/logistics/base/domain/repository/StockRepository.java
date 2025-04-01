package com.logistics.base.domain.repository;

import com.logistics.base.domain.model.Stock;

import java.util.Set;

public interface StockRepository {

  Set<Stock> findByBarcodes(Set<String> barcodes);

  Set<Stock> findByStorageUuidAndBarcodes(String storageUuid, Set<String> barcodes);

  Set<Stock> saveAll(Set<Stock> stocks);
}
