package com.logistics.base.controller.dto;

import java.time.LocalDate;

public record GenerateStockDTO(
  LocalDate expirationDate,
  Integer quantity) {
}
