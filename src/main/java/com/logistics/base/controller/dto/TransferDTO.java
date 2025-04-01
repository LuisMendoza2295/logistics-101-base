package com.logistics.base.controller.dto;

import java.util.Set;

public record TransferDTO(
  String uuid,
  StorageUnitDTO sourceStorageUnitDTO,
  StorageUnitDTO targetStorageUnitDTO,
  Set<StockDTO> stockDTOs) {
}
