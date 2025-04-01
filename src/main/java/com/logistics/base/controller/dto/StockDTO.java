package com.logistics.base.controller.dto;

import java.time.LocalDate;

public record StockDTO(
  String barcode,
  LocalDate expirationDate,
  ProductDTO product,
  StorageUnitDTO storageUnit) {
}
