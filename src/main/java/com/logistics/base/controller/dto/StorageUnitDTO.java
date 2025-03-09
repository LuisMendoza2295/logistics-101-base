package com.logistics.base.controller.dto;

import java.math.BigDecimal;
import java.util.Set;

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
    Set<ProductDTO> products) {

}
