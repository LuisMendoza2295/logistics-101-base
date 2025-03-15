package com.logistics.base.controller.dto;

import java.time.LocalDate;

public record GenerateStorageStockDTO(
    String productUUID,
    LocalDate expirationDate,
    Integer quantity) {
}
