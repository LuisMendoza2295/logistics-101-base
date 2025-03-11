package com.logistics.base.domain;

import java.time.LocalDate;
import java.util.Set;

public interface LogisticAggregate {

    Product findByProductUuid(String uuid);

    Product persistProduct(Product product);

    StorageUnit persistStorageUnit(StorageUnit storageUnit);

    StorageUnit addProducts(String storageUnitUUID, Set<String> barcodes);

    Set<StorageUnit> findByType(String storageType);

    Transfer transferProduct(String sourceUUID, String targetUUID, Set<String> barcodes);

    Set<Stock> generateStocks(String storageUnitUUID, String productUUID, LocalDate expirationDate, Integer quantity);
}
