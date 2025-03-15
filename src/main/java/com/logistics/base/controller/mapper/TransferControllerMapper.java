package com.logistics.base.controller.mapper;

import com.logistics.base.controller.dto.CreateTransferDTO;
import com.logistics.base.controller.dto.TransferDTO;
import com.logistics.base.domain.Transfer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

@ApplicationScoped
public class TransferControllerMapper {

    @Inject
    StockControllerMapper stockControllerMapper;
    @Inject
    StorageControllerMapper storageControllerMapper;

    public TransferDTO toTransferDTO(Transfer transfer) {
        return new TransferDTO(
            transfer.uuid().toString(),
            storageControllerMapper.toStorageUnitDTO(transfer.source()),
            storageControllerMapper.toStorageUnitDTO(transfer.target()),
            transfer.stocks().stream()
                .map(stock -> stockControllerMapper.toStockDTO(stock))
                .collect(Collectors.toSet())
        );
    }

    public Transfer toTransfer(TransferDTO transferDTO) {
        var builder = Transfer.builder()
            .uuid(transferDTO.uuid())
            .source(storageControllerMapper.toStorageUnit(transferDTO.sourceStorageUnitDTO()))
            .target(storageControllerMapper.toStorageUnit(transferDTO.targetStorageUnitDTO()))
            .build();
        transferDTO.stockDTOs()
            .forEach(stockDTO -> stockControllerMapper.toStock(stockDTO));
        return builder;
    }
}
