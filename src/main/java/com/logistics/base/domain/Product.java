package com.logistics.base.domain;

import uk.org.okapibarcode.backend.Code128;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record Product(
    Long id,
    UUID uuid,
    String name,
    Dimensions dimensions,
    BigDecimal netWeight,
    BigDecimal grossWeight) {

  public static final class Builder {
    private Long id;
    private UUID uuid;
    private String name;
    private Dimensions dimensions;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    public Builder uuid(String uuid) {
      this.uuid = Optional.ofNullable(uuid).map(UUID::fromString).orElse(UUID.randomUUID());
      return this;
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
          id,
          Optional.ofNullable(uuid).orElse(UUID.randomUUID()),
          name,
          dimensions,
          netWeight,
          grossWeight);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.id = id;
    builder.uuid = this.uuid;
    builder.name = this.name;
    builder.dimensions = this.dimensions;
    builder.netWeight = this.netWeight;
    builder.grossWeight = this.grossWeight;
    return builder;
  }

  public Stock generateStock(LocalDate expirationDate) {
    Code128 barcode = new Code128();
    barcode.setContent(UUID.randomUUID().toString());
    return Stock.builder()
        .barcode(barcode.getContent())
        .expirationDate(expirationDate)
        .product(this)
        .storageUnit(null)
        .build();
  }
}
