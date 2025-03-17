package com.logistics.base.repository.mapper;

import com.logistics.base.domain.Transfer;
import com.logistics.base.repository.entity.TransferEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransferDbMapper {

    @Inject
    StorageUniDbMapper storageUniDbMapper;
    @Inject
    StockDbMapper stockDbMapper;

    public TransferEntity toTransferEntity(Transfer transfer) {
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setId(transfer.id());
        transferEntity.setUuid(transfer.uuid().toString());
        transferEntity.setSourceStorage(storageUniDbMapper.toStorageUnitEntity(transfer.source()));
        transferEntity.setTargetStorage(storageUniDbMapper.toStorageUnitEntity(transfer.source()));
        transfer.stocks().stream()
            .map(stock -> stockDbMapper.toStockEntity(stock))
            .forEach(transferEntity::addStock);
        return transferEntity.getAttachedEntity();
    }

    public Transfer toTransfer(TransferEntity transferEntity) {
        var transferBuilder = Transfer.builder()
            .id(transferEntity.id())
            .uuid(transferEntity.uuid())
            .source(storageUniDbMapper.toStorageUnit(transferEntity.sourceStorage()))
            .target(storageUniDbMapper.toStorageUnit(transferEntity.targetStorage()));
        transferEntity.stocks().stream()
            .map(stockEntity -> stockDbMapper.toStock(stockEntity))
            .forEach(transferBuilder::addStock);
        return transferBuilder.build();
    }
}
