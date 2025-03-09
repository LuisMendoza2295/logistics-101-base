package com.logistics.base.domain;

import java.util.Map;
import java.util.Set;

public interface LogisticAggregate {

    Product findByProductUuid(String uuid);

    Product persist(Product product);

    StorageUnit persist(StorageUnit storageUnit);

    StorageUnit addProducts(String storageUnitUUID, Map<String, Integer> productUUIDsWithQty);

    Set<StorageUnit> findByType(String storageType);

    Transfer transferProduct(String sourceUUID, String targetUUID, Map<String, Integer> productsUUIDsWithQty);
}
