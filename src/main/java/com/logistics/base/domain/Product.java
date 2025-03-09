package com.logistics.base.domain;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record Product(
    UUID uuid,
    String name,
    Dimensions dimensions,
    BigDecimal netWeight,
    BigDecimal grossWeight) {

  public Product(
      String uuid,
      String name,
      Dimensions dimensions,
      BigDecimal netWeight,
      BigDecimal grossWeight) {
    this(Optional.ofNullable(uuid).map(UUID::fromString).orElse(UUID.randomUUID()),
        name,
        dimensions,
        netWeight,
        grossWeight);
  }

  public static final class Builder {
    private UUID uuid;
    private String name;
    private Dimensions dimensions;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;

    private Builder() {
    }

    public Builder uuid(UUID uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder dimensions(Dimensions dimensions) {
      this.dimensions = dimensions;
      return this;
    }

    public Builder netWeight(BigDecimal netWeight) {
      this.netWeight = netWeight;
      return this;
    }

    public Builder grossWeight(BigDecimal grossWeight) {
      this.grossWeight = grossWeight;
      return this;
    }

    public Product build() {
      return new Product(
          uuid,
          name,
          dimensions,
          netWeight,
          grossWeight);
    }
  }
}
