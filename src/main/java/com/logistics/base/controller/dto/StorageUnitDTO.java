package com.logistics.base.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.logistics.base.controller.mapper.ProductWebMapper;

import java.math.BigDecimal;
import java.util.Map;

public record StorageUnitDTO(
  String uuid,
  String storageType,
  BigDecimal height,
  BigDecimal width,
  BigDecimal length,
  BigDecimal weightCapacity,
  BigDecimal volumeOccupied,
  BigDecimal weightOccupied,
  int maxUnits,
  String storageStatus,
  @JsonDeserialize(keyUsing = ProductWebMapper.class)
  Map<ProductDTO, Integer> productsWithQty) {
}
