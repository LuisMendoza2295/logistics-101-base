package com.logistics.base.repository.mapper;

import com.logistics.base.domain.model.Transfer;
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
    TransferEntity temp = new TransferEntity();
    temp.setId(transfer.id());

    TransferEntity transferEntity = temp.getAttachedEntity();
    transferEntity.setUuid(transfer.uuid().toString());
    transferEntity.setSourceStorage(storageUniDbMapper.toStorageUnitEntity(transfer.source()));
    transferEntity.setTargetStorage(storageUniDbMapper.toStorageUnitEntity(transfer.target()));
    transfer.stocks().stream()
      .map(stockDbMapper::toStockEntity)
      .forEach(transferEntity::addStock);
    return transferEntity;
  }

  public Transfer toTransfer(TransferEntity transferEntity) {
    var builder = Transfer.builder()
      .id(transferEntity.id())
      .uuid(transferEntity.uuid())
      .source(storageUniDbMapper.toStorageUnit(transferEntity.sourceStorage()))
      .target(storageUniDbMapper.toStorageUnit(transferEntity.targetStorage()));
    transferEntity.stocks().stream()
      .map(stockDbMapper::toStock)
      .forEach(builder::addStock);
    return builder.build();
  }
}
