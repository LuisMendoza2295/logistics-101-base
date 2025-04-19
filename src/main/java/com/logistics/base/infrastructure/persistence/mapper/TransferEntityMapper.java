package com.logistics.base.infrastructure.persistence.mapper;

import com.logistics.base.domain.model.Transfer;
import com.logistics.base.infrastructure.persistence.entity.TransferEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransferEntityMapper {

  @Inject
  StorageUnitEntityMapper storageUnitEntityMapper;
  @Inject
  StockEntityMapper stockEntityMapper;

  public TransferEntity toTransferEntity(Transfer transfer) {
    TransferEntity temp = new TransferEntity();
    temp.setId(transfer.id());

    TransferEntity transferEntity = temp.getAttachedEntity();
    transferEntity.setUuid(transfer.uuid().toString());
    transferEntity.setSourceStorage(storageUnitEntityMapper.toStorageUnitEntity(transfer.source()));
    transferEntity.setTargetStorage(storageUnitEntityMapper.toStorageUnitEntity(transfer.target()));
    transfer.stocks().stream()
      .map(stockEntityMapper::toStockEntity)
      .forEach(transferEntity::addStock);
    return transferEntity;
  }

  public Transfer toTransfer(TransferEntity transferEntity) {
    var builder = Transfer.builder()
      .id(transferEntity.id())
      .uuid(transferEntity.uuid())
      .source(storageUnitEntityMapper.toStorageUnit(transferEntity.sourceStorage()))
      .target(storageUnitEntityMapper.toStorageUnit(transferEntity.targetStorage()));
    transferEntity.stocks().stream()
      .map(stockEntityMapper::toStock)
      .forEach(builder::addStock);
    return builder.build();
  }
}
