package com.logistics.base.controller.dto;

import java.math.BigDecimal;

public record ProductDTO(
  String uuid,
  String name,
  BigDecimal height,
  BigDecimal width,
  BigDecimal length,
  BigDecimal netWeight,
  BigDecimal grossWeight) {
}
