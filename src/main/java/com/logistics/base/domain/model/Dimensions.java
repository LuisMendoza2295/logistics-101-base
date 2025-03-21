package com.logistics.base.domain.model;

import java.math.BigDecimal;

public record Dimensions(
    BigDecimal width,
    BigDecimal height,
    BigDecimal length) {

  public Dimensions {
    if (width == null || height == null || length == null) {
      throw new IllegalArgumentException("Dimensions cannot be null");
    }
    if (width.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Value cannot be zero or negative");
    }
    if (height.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Value cannot be zero or negative");
    }
    if (length.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Value cannot be zero or negative");
    }
  }

  public BigDecimal getVolume() {
    return width.multiply(height).multiply(length);
  }
}
