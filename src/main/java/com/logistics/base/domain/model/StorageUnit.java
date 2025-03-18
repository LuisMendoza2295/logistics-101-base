package com.logistics.base.domain.model;

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

  public StorageUnit addStock(Stock stock) {
    return addStocks(Set.of(stock));
  }

  public StorageUnit addStocks(Set<Stock> stocks) {
    BigDecimal totalVolume = dimensions.getVolume();
    BigDecimal space = calculateOccupiedSpace();
    BigDecimal weight = weightOccupied;

    for (Stock stock : stocks) {
      space = space.add(validateSpace(stock.product(), 1, space, totalVolume));
      weight = weight.add(validateWeight(stock.product(), 1, weight));
    }

    var builder = this.toBuilder()
        .volumeOccupied(space)
        .weightOccupied(weight);
    stocks.forEach(builder::addStock);
    return builder.build();
  }

  public StorageUnit addProduct(Product product, int quantity) {
    BigDecimal totalVolume = dimensions.getVolume();
    BigDecimal space = calculateOccupiedSpace();
    BigDecimal weight = weightOccupied;

    space = space.add(validateSpace(product, quantity, space, totalVolume));
    weight = weight.add(validateWeight(product, quantity, weight));

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

  public StorageUnit removeProducts(Set<Product> products) {
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

  private BigDecimal validateSpace(Product product, int quantity, BigDecimal space, BigDecimal totalVolume) {
    BigDecimal productVolume = product.dimensions().getVolume().multiply(BigDecimal.valueOf(quantity));
    space = space.add(productVolume);
    if (space.compareTo(totalVolume) > 0) {
      throw new RuntimeException("No space available for product " + product.name());
    }
    return space;
  }

  private BigDecimal validateWeight(Product product, int quantity, BigDecimal weight) {
    BigDecimal productWeight = product.grossWeight().multiply(BigDecimal.valueOf(quantity));
    weight = weight.add(productWeight);
    if (weight.compareTo(weightCapacity) > 0) {
      throw new RuntimeException("Weight support is not available for product " + product.name());
    }
    return weight;
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
