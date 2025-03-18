package com.logistics.base.domain.model;

import java.math.BigDecimal;

public record Dimensions(
    BigDecimal width,
    BigDecimal height,
    BigDecimal length) {

  public BigDecimal getVolume() {
    return width.multiply(height).multiply(length);
  }
}
