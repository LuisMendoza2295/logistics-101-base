package com.logistics.base.domain.aggregate;

import com.logistics.base.domain.model.Product;
import com.logistics.base.domain.model.Stock;
import com.logistics.base.domain.model.StorageUnit;
import com.logistics.base.domain.model.Transfer;

import java.time.LocalDate;
import java.util.Set;

public interface LogisticAggregate {

    Product findByProductUuid(String uuid);

    Product persistProduct(Product product);

    StorageUnit findByStorageUnitUuid(String storageUnitUUID);

    StorageUnit persistStorageUnit(StorageUnit storageUnit);

    StorageUnit addProducts(String storageUnitUUID, Set<String> barcodes);

    Set<StorageUnit> findByType(String storageType);

    Transfer transferProduct(String sourceStorageUUID, String targetStorageUUID, Set<String> barcodes);

    Set<Stock> generateStocks(String storageUnitUUID, String productUUID, LocalDate expirationDate, Integer quantity);

    Set<Stock> generateStocks(String productUUID, LocalDate expirationDate, Integer quantity);
}
