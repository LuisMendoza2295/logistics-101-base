package com.logistics.base.domain;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;

public record StorageUnit(
    UUID uuid,
    StorageType storageType,
    Dimensions dimensions,
    BigDecimal weightCapacity,
    BigDecimal volumeOccupied,
    BigDecimal weightOccupied,
    int maxUnits,
    StorageStatus storageStatus,
    Map<Product, Integer> productsWithQty) {

  public StorageUnit(
      String uuid,
      String storageType,
      Dimensions dimensions,
      BigDecimal weightCapacity,
      BigDecimal volumeOccupied,
      BigDecimal weightOccupied,
      int maxUnits,
      String storageStatus) {
    this(Optional.ofNullable(uuid).map(UUID::fromString).orElse(UUID.randomUUID()),
        StorageType.valueOf(storageType),
        dimensions, weightCapacity,
        volumeOccupied,
        weightOccupied,
        maxUnits,
        StorageStatus.valueOf(storageStatus),
        new HashMap<>());
  }

  public static final class Builder {
    private UUID uuid;
    private StorageType storageType;
    private Dimensions dimensions;
    private BigDecimal weightCapacity;
    private BigDecimal volumeOccupied;
    private BigDecimal weightOccupied;
    private int maxUnits;
    private StorageStatus storageStatus;
    private final Map<Product, Integer> productsWithQty = new HashMap<>();

    private Builder() {}

    public Builder uuid(UUID uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder storageType(StorageType storageType) {
      this.storageType = storageType;
      return this;
    }

    public Builder dimensions(Dimensions dimensions) {
      this.dimensions = dimensions;
      return this;
    }

    public Builder weightCapacity(BigDecimal weightCapacity) {
      this.weightCapacity = weightCapacity;
      return this;
    }

    public Builder volumeOccupied(BigDecimal volumeOccupied) {
      this.volumeOccupied = volumeOccupied;
      return this;
    }

    public Builder weightOccupied(BigDecimal weightOccupied) {
      this.weightOccupied = weightOccupied;
      return this;
    }

    public Builder maxUnits(int maxUnits) {
      this.maxUnits = maxUnits;
      return this;
    }

    public Builder storageStatus(StorageStatus storageStatus) {
      this.storageStatus = storageStatus;
      return this;
    }

    public Builder addProduct(Product product, Integer quantity) {
      this.productsWithQty.put(product, quantity);
      return this;
    }

    public StorageUnit build() {
      return new StorageUnit(
          uuid,
          storageType,
          dimensions,
          weightCapacity,
          volumeOccupied,
          weightOccupied,
          maxUnits,
          storageStatus,
          productsWithQty
      );
    }
  }

  public Builder toBuilder() {
    Builder builder = new Builder()
        .uuid(uuid)
        .storageType(storageType)
        .dimensions(dimensions)
        .weightCapacity(weightCapacity)
        .volumeOccupied(volumeOccupied)
        .weightOccupied(weightOccupied)
        .maxUnits(maxUnits)
        .storageStatus(storageStatus);
    productsWithQty.forEach(builder::addProduct);
    return builder;
  }

  public BigDecimal availableSpace() {
    return dimensions.getVolume().subtract(volumeOccupied);
  }

  public BigDecimal availableWeight() {
    return weightCapacity.subtract(weightOccupied);
  }

  public BigDecimal calculateOccupiedSpace() {
    return productsWithQty.keySet().stream()
        .map(product -> product.dimensions().getVolume())
        .reduce(ZERO, BigDecimal::add);
  }

  public StorageUnit addProduct(Product product, Integer quantity) {
    BigDecimal availableSpace = dimensions.getVolume().subtract(calculateOccupiedSpace());
    BigDecimal productVolume = product.dimensions().getVolume();
    if (availableSpace.compareTo(productVolume) < 0) {
      throw new RuntimeException("No space available for product " + product.name());
    }
    BigDecimal productWeight = product.grossWeight();
    if (availableWeight().compareTo(productWeight) < 0) {
      throw new RuntimeException("Weight support is not available for product " + product.name());
    }
    return this.toBuilder()
        .addProduct(product, quantity)
        .volumeOccupied(productVolume)
        .weightOccupied(productWeight)
        .build();
  }

  public StorageUnit addProducts(Map<Product, Integer> productsWithQty) {
    Builder builder = this.toBuilder();
    productsWithQty.forEach(builder::addProduct);
    return builder.build();
  }
}
