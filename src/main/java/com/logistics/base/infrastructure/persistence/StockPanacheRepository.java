package com.logistics.base.infrastructure.persistence;

import com.logistics.base.infrastructure.persistence.entity.StockEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class StockPanacheRepository implements PanacheRepository<StockEntity> {

  public Set<StockEntity> findByBarcodes(List<String> barcodes) {
    return find("FROM stocks WHERE barcode in ?1", barcodes).stream()
      .collect(Collectors.toSet());
  }

  public Set<StockEntity> findByStorageUuidAndBarcodes(String storageUuid, List<String> barcodes) {
    return find("FROM stocks WHERE storageUnit.uuid = ?1 AND barcode in ?2", storageUuid, barcodes).stream()
      .collect(Collectors.toSet());
  }
}
