package com.logistics.base.domain.model;

import uk.org.okapibarcode.backend.Code128;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.logistics.base.domain.model.Dimensions.ZERO_DIMENSION;
import static com.logistics.base.domain.model.StorageStatus.AVAILABLE;
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

  public StorageUnit {
    if (uuid == null) {
      throw new IllegalArgumentException("uuid cannot be null");
    }
    if (storageType == null) {
      throw new IllegalArgumentException("storageType cannot be null");
    }
    if (dimensions == null) {
      throw new IllegalArgumentException("dimensions cannot be null");
    }
    if (dimensions.hasInvalidDimensions()) {
      throw new IllegalArgumentException("Value cannot be zero or negative");
    }
    if (weightCapacity == null) {
      throw new IllegalArgumentException("weightCapacity cannot be null");
    }
    if (maxUnits <= 1) {
      throw new IllegalArgumentException("maxUnits cannot be less than 1");
    }
  }

  public static Builder builder() {
    return new Builder();
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

  public Dimensions calculateOccupiedDimensions() {
    return productsWithQty.entrySet().stream()
      .map(productEntry -> productEntry.getKey().dimensions().multiply(productEntry.getValue()))
      .reduce(ZERO_DIMENSION, Dimensions::add);
  }

  public StorageUnit addStocks(Set<Stock> stocks) {
    BigDecimal space = volumeOccupied;
    BigDecimal weight = weightOccupied;

    for (Stock stock : stocks) {
      space = space.add(validateSpace(stock));
      weight = weight.add(validateWeight(stock));
    }

    return stocks.stream()
      .map(this::addStock)
      .reduce(this, (first, second) -> second);
  }

  public StorageUnit addStock(Stock stock) {
    return addProduct(stock.product(), 1);
  }

  public StorageUnit addProduct(Product product, int quantity) {
    BigDecimal space = volumeOccupied.add(validateSpace(product, quantity));
    BigDecimal weight = weightOccupied.add(validateWeight(product, quantity));

    var builder = this.toBuilder()
      .volumeOccupied(space)
      .weightOccupied(weight);
    builder.removeProduct(product, quantity);
    return builder.build();
  }

  public StorageUnit removeStocks(Set<Stock> stocks) {
    BigDecimal space = ZERO;
    BigDecimal weight = ZERO;
    for (Stock stock : stocks) {
      space = space.add(stock.product().dimensions().getVolume());
      weight = weight.add(stock.product().grossWeight());
    }
    BigDecimal totalVolume = dimensions.getVolume();
    var builder = this.toBuilder()
      .volumeOccupied(totalVolume.subtract(space))
      .weightOccupied(weightOccupied.subtract(weight));
    stocks.forEach(builder::removeStock);
    return builder.build();
  }

  public StorageUnit removeProduct(Set<Product> products) {
    BigDecimal space = ZERO;
    BigDecimal weight = ZERO;
    for (Product product : products) {
      space = space.add(product.dimensions().getVolume());
      weight = weight.add(product.grossWeight());
    }
    BigDecimal totalVolume = dimensions.getVolume();
    var builder = this.toBuilder()
      .volumeOccupied(totalVolume.subtract(space))
      .weightOccupied(weightOccupied.subtract(weight));
    products.forEach(builder::removeProduct);
    return builder.build();
  }

  private BigDecimal validateSpace(Stock stock) {
    return validateSpace(stock.product(), 1);
  }

  private BigDecimal validateSpace(Product product, int quantity) {
    Dimensions productWithQty = product.calculateDimensions(quantity);
    if (!this.dimensions.canFit(productWithQty)) {
      throw new RuntimeException("Product " + product.name() + " does not fit dimensions");
    }

    BigDecimal remainingSpace = this.availableSpace();
    BigDecimal productVolume = productWithQty.getVolume();
    if (productVolume.compareTo(remainingSpace) > 0) {
      throw new RuntimeException("No space available for product " + product.name());
    }
    return productVolume;
  }

  private BigDecimal validateWeight(Stock stock) {
    return validateWeight(stock.product(), 1);
  }

  private BigDecimal validateWeight(Product product, int quantity) {
    BigDecimal remainingWeight = this.availableWeight();

    BigDecimal productWeight = product.calculateGrossWeight(quantity);
    if (productWeight.compareTo(remainingWeight) > 0) {
      throw new RuntimeException("Weight support is not available for product " + product.name());
    }
    return productWeight;
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

  public static final class Builder {
    private final Map<Product, Integer> productsWithQty = new HashMap<>();
    private Long id;
    private UUID uuid;
    private StorageType storageType;
    private Dimensions dimensions;
    private BigDecimal weightCapacity;
    private BigDecimal volumeOccupied;
    private BigDecimal weightOccupied;
    private int maxUnits;
    private StorageStatus storageStatus;

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

    public Builder removeStock(Stock stock) {
      this.removeProduct(stock.product());
      return this;
    }

    public Builder removeProduct(Product product) {
      this.removeProduct(product, 1);
      return this;
    }

    public Builder removeProduct(Product product, int quantity) {
      if (productsWithQty.containsKey(product)) {
        int updatedQuantity = productsWithQty.get(product) - quantity;
        if (updatedQuantity == 0) {
          productsWithQty.remove(product);
          return this;
        }
        productsWithQty.put(product, updatedQuantity);
      }
      return this;
    }

    public StorageUnit build() {
      return new StorageUnit(
        id,
        Optional.ofNullable(uuid).orElse(UUID.randomUUID()),
        storageType,
        dimensions,
        weightCapacity,
        Optional.ofNullable(volumeOccupied).orElse(ZERO),
        Optional.ofNullable(weightOccupied).orElse(ZERO),
        maxUnits,
        Optional.ofNullable(storageStatus).orElse(AVAILABLE),
        productsWithQty
      );
    }
  }
}
