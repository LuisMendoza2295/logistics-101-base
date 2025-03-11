package com.logistics.base.controller.dto;

import java.time.LocalDate;

public record GenerateStockDTO(
    String productUUID,
    LocalDate expirationDate,
    Integer quantity) {
}
