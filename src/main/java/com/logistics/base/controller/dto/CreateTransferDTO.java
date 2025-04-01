package com.logistics.base.controller.dto;

import java.util.Set;

public record CreateTransferDTO(
  String sourceStorageUUID,
  String targetStorageUUID,
  Set<String> barcodes) {
}
