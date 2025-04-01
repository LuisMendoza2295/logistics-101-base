package com.logistics.base.domain.repository;

import com.logistics.base.domain.model.Transfer;

import java.util.Optional;
import java.util.Set;

public interface TransferRepository {

  Optional<Transfer> findByUuid(String uuid);

  Set<Transfer> findBySourceStorage(String sourceStorageUUID);

  Transfer save(Transfer transfer);
}
