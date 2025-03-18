package com.logistics.base.repository;

import com.logistics.base.domain.model.Transfer;
import com.logistics.base.domain.repository.TransferRepository;
import com.logistics.base.repository.entity.TransferEntity;
import com.logistics.base.repository.mapper.TransferDbMapper;
import com.logistics.base.repository.panache.TransferPanacheRepository;
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
    TransferDbMapper transferDbMapper;

    @Override
    public Optional<Transfer> findByUuid(String uuid) {
        return transferPanacheRepository.findByUuid(uuid)
            .map(transferDbMapper::toTransfer);
    }

    @Override
    public Set<Transfer> findBySourceStorage(String sourceStorageUUID) {
        return transferPanacheRepository.findBySourceStorage(sourceStorageUUID).stream()
            .map(transferDbMapper::toTransfer)
            .collect(Collectors.toSet());
    }

    @Override
    public Transfer save(Transfer transfer) {
        TransferEntity transferEntity = transferDbMapper.toTransferEntity(transfer);
        transferPanacheRepository.persist(transferEntity);
        return transferDbMapper.toTransfer(transferEntity);
    }
}
