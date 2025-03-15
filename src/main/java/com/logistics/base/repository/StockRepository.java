package com.logistics.base.repository;

import com.logistics.base.repository.entity.StockEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class StockRepository implements PanacheRepository<StockEntity> {

    public List<StockEntity> findByBarcodes(List<String> barcodes) {
        return list("FROM stocks WHERE barcode in ?1", barcodes);
    }

    public List<StockEntity> findByStorageUuidAndBarcodes(String storageUuid, List<String> barcodes) {
        return list("FROM stocks WHERE storageUnit.uuid = ?1 AND barcode in ?2", storageUuid, barcodes);
    }
}
