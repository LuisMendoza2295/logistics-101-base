package com.logistics.base.controller.dto;

import java.time.LocalDate;
import java.util.Set;

public record StockBatchDTO(
  String productUUID,
  LocalDate expirationDate,
  Set<String> barcodes) {
}
