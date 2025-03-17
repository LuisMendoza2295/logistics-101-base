package com.logistics.base.domain;

import uk.org.okapibarcode.backend.Code128;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.math.BigDecimal.ZERO;

public record StorageUnit(
    Long id,
    UUID uuid,
    StorageType storageType,
    Dimensions dimensions,
    BigDecimal weightCapacity,
    BigDecimal volumeOccupied,
    BigDecimal weightOccupied,
    int maxUnits,
    StorageStatus storageStatus,
    Map<Product, Integer> productsWithQty) {

  public static final class Builder {
    private Long id;
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
    public Builder storageType(String storageType) {
      this.storageType = StorageType.valueOf(storageType);
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
    public Builder storageStatus(String storageStatus) {
      this.storageStatus = StorageStatus.valueOf(storageStatus);
      return this;
    }
    public Builder storageStatus(StorageStatus storageStatus) {
      this.storageStatus = storageStatus;
      return this;
    }
    public Builder addStock(Stock stock) {
      this.addProduct(stock.product());
      return this;
    }
    public Builder addProduct(Product product) {
      this.addProduct(product, 1);
      return this;
    }
    public Builder addProduct(Product product, int quantity) {
      if (productsWithQty.containsKey(product)) {
        productsWithQty.put(product, productsWithQty.get(product) + quantity);
      } else {
        productsWithQty.put(product, quantity);
      }
      return this;
    }

    public StorageUnit build() {
      return new StorageUnit(
          id,
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
        .id(id)
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

  public static Builder builder() {
    return new Builder();
  }

  public BigDecimal availableSpace() {
    return dimensions.getVolume().subtract(volumeOccupied);
  }

  public BigDecimal availableWeight() {
    return weightCapacity.subtract(weightOccupied);
  }

  public BigDecimal calculateOccupiedSpace() {
    return productsWithQty.entrySet().stream()
        .map(productEntry ->
            productEntry.getKey().dimensions().getVolume()
                .multiply(BigDecimal.valueOf(productEntry.getValue())))
        .reduce(ZERO, BigDecimal::add);
  }

  public StorageUnit addProduct(Stock stock) {
    return addProducts(Set.of(stock));
  }

  public StorageUnit addProducts(Set<Stock> stocks) {
    BigDecimal totalVolume = dimensions.getVolume();
    BigDecimal availableSpace = totalVolume.subtract(calculateOccupiedSpace());
    BigDecimal availableWeight = availableWeight();
    for (Stock stock : stocks) {
      BigDecimal productVolume = stock.product().dimensions().getVolume();
      if (availableSpace.compareTo(productVolume) < 0) {
        throw new RuntimeException("No space available for product " + stock.product().name());
      }
      BigDecimal productWeight = stock.product().grossWeight();
      if (availableWeight.compareTo(productWeight) < 0) {
        throw new RuntimeException("Weight support is not available for product " + stock.product().name());
      }
      availableSpace = availableSpace.subtract(productVolume);
      availableWeight = availableWeight.subtract(productWeight);
    }
    var builder = this.toBuilder()
        .volumeOccupied(totalVolume.subtract(availableSpace))
        .weightOccupied(weightCapacity.subtract(availableWeight));
    stocks.forEach(builder::addStock);
    return builder.build();
  }

  public Stock generateStock(Product product, LocalDate expirationDate) {
    Code128 barcode = new Code128();
    barcode.setContent(UUID.randomUUID().toString());
    return Stock.builder()
        .barcode(barcode.getContent())
        .expirationDate(expirationDate)
        .product(product)
        .storageUnit(this)
        .build();
  }
}
