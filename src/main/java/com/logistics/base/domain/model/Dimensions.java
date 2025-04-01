package com.logistics.base.domain.model;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static java.math.BigDecimal.ZERO;

public record Dimensions(
  BigDecimal width,
  BigDecimal height,
  BigDecimal length) {

  public static final Dimensions ZERO_DIMENSION = new Dimensions(ZERO, ZERO, ZERO);

  public Dimensions {
    if (width == null || height == null || length == null) {
      throw new IllegalArgumentException("Dimensions cannot be null");
    }
    if (width.compareTo(ZERO) < 0) {
      throw new IllegalArgumentException("Value cannot be negative");
    }
    if (height.compareTo(ZERO) < 0) {
      throw new IllegalArgumentException("Value cannot be negative");
    }
    if (length.compareTo(ZERO) < 0) {
      throw new IllegalArgumentException("Value cannot be negative");
    }
  }

  public static Dimensions.Builder builder() {
    return new Dimensions.Builder();
  }

  public Dimensions.Builder toBuilder() {
    Dimensions.Builder builder = new Dimensions.Builder();
    builder.width(width);
    builder.height(height);
    builder.length(length);
    return builder;
  }

  public BigDecimal getVolume() {
    return width.multiply(height).multiply(length);
  }

  public boolean hasInvalidDimensions() {
    return width.compareTo(ZERO) <= 0 || height.compareTo(ZERO) <= 0 || length.compareTo(ZERO) <= 0;
  }

  public boolean canFit(Dimensions dimensions) {
    if (!canFit(dimensions.width) || !canFit(dimensions.height) || !canFit(dimensions.length)) {
      return false;
    }
    return this.getVolume().compareTo(dimensions.getVolume()) > 0;
  }

  public boolean canFit(BigDecimal dimensionComponent) {
    return dimensionComponent.compareTo(this.width) < 0 && dimensionComponent.compareTo(this.height) < 0 && dimensionComponent.compareTo(this.length) < 0;
  }

  public Dimensions add(Dimensions dimensions) {
    return this.toBuilder()
      .width(this.width.add(dimensions.width))
      .height(this.height.add(dimensions.height))
      .length(this.length.add(dimensions.length))
      .build();
  }

  public Dimensions subtract(Dimensions dimensions) {
    return this.toBuilder()
      .width(this.width.subtract(dimensions.width))
      .height(this.height.add(dimensions.height))
      .length(this.length.subtract(dimensions.length))
      .build();
  }

  public Dimensions multiply(int multiplier) {
    return IntStream.range(0, multiplier)
      .mapToObj(i -> this)
      .reduce(ZERO_DIMENSION, Dimensions::add);
  }

  public static final class Builder {
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal length;

    private Builder() {
    }

    public Builder width(BigDecimal width) {
      this.width = width;
      return this;
    }

    public Builder height(BigDecimal height) {
      this.height = height;
      return this;
    }

    public Builder length(BigDecimal length) {
      this.length = length;
      return this;
    }

    public Dimensions build() {
      return new Dimensions(width, height, length);
    }
  }
}
