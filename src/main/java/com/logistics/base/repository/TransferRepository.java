package com.logistics.base.repository;

import com.logistics.base.repository.entity.TransferEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TransferRepository implements PanacheRepository<TransferEntity> {

  public TransferEntity findByUuid(String uuid) {
    return find("uuid", uuid).firstResult();
  }

  public TransferEntity findBySourceStorage(String sourceStorageUUID) {
    return find("SELECT t " +
      "FROM TransferEntity t " +
      "WHERE t.sourceStorage = :uuid", sourceStorageUUID).firstResult();
  }
}
