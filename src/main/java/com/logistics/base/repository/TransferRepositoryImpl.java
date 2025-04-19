package com.logistics.base.repository;

import com.logistics.base.domain.model.Transfer;
import com.logistics.base.domain.repository.TransferRepository;
import com.logistics.base.infrastructure.persistence.entity.TransferEntity;
import com.logistics.base.infrastructure.persistence.mapper.TransferEntityMapper;
import com.logistics.base.infrastructure.persistence.TransferPanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransferRepositoryImpl implements TransferRepository {

  @Inject
  TransferPanacheRepository transferPanacheRepository;
  @Inject
  TransferEntityMapper transferEntityMapper;

  @Override
  public Optional<Transfer> findByUuid(String uuid) {
    return transferPanacheRepository.findByUuid(uuid)
      .map(transferEntityMapper::toTransfer);
  }

  @Override
  public Set<Transfer> findBySourceStorage(String sourceStorageUUID) {
    return transferPanacheRepository.findBySourceStorage(sourceStorageUUID).stream()
      .map(transferEntityMapper::toTransfer)
      .collect(Collectors.toSet());
  }

  @Override
  public Transfer save(Transfer transfer) {
    TransferEntity transferEntity = transferEntityMapper.toTransferEntity(transfer);
    transferPanacheRepository.persist(transferEntity);
    return transferEntityMapper.toTransfer(transferEntity);
  }
}
