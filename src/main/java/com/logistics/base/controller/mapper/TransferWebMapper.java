package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.TransferDTO;
import com.logistics.base.domain.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

@ApplicationScoped
public class TransferWebMapper {

    @Inject
    StockWebMapper stockWebMapper;
    @Inject
    StorageWebMapper storageWebMapper;

    public TransferDTO toTransferDTO(Transfer transfer) {
        return new TransferDTO(
            transfer.uuid().toString(),
            storageWebMapper.toStorageUnitDTO(transfer.source()),
            storageWebMapper.toStorageUnitDTO(transfer.target()),
            transfer.stocks().stream()
                .map(stock -> stockWebMapper.toStockDTO(stock))
                .collect(Collectors.toSet())
        );
    }

    public Transfer toTransfer(TransferDTO transferDTO) {
        var builder = Transfer.builder()
            .uuid(transferDTO.uuid())
            .source(storageWebMapper.toStorageUnit(transferDTO.sourceStorageUnitDTO()))
            .target(storageWebMapper.toStorageUnit(transferDTO.targetStorageUnitDTO()))
            .build();
        transferDTO.stockDTOs()
            .forEach(stockDTO -> stockWebMapper.toStock(stockDTO));
        return builder;
    }
}
