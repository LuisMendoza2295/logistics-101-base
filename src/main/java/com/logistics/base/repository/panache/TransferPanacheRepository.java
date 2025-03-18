package com.logistics.base.repository.panache;

import com.logistics.base.repository.entity.TransferEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransferPanacheRepository implements PanacheRepository<TransferEntity> {

  public Optional<TransferEntity> findByUuid(String uuid) {
    return find("uuid", uuid).firstResultOptional();
  }

  public Set<TransferEntity> findBySourceStorage(String sourceStorageUUID) {
    return find("SELECT t " +
      "FROM TransferEntity t " +
      "WHERE t.sourceStorage = :uuid", sourceStorageUUID).stream().collect(Collectors.toSet());
  }
}
