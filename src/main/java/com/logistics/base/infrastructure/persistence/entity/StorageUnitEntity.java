package com.logistics.base.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "storageUnits")
@Table(
  name = "storage_units",
  indexes = {
    @Index(name = "idx_storage_type", columnList = "storageType"),
    @Index(name = "idx_storage_status", columnList = "storageStatus")
  })
public class StorageUnitEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String uuid;
  private String storageType;
  private String storageStatus;
  private BigDecimal height;
  private BigDecimal width;
  private BigDecimal length;
  private BigDecimal weightCapacity;
  private BigDecimal volumeOccupied;
  private BigDecimal weightOccupied;
  private Integer maxUnits;
  @Transient
  private Map<ProductEntity, Integer> productsWithQty = new HashMap<>();

  public Long id() {
    return id;
  }

  public StorageUnitEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String uuid() {
    return uuid;
  }

  public StorageUnitEntity setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public String storageType() {
    return storageType;
  }

  public StorageUnitEntity setStorageType(String storageType) {
    this.storageType = storageType;
    return this;
  }

  public String storageStatus() {
    return storageStatus;
  }

  public StorageUnitEntity setStorageStatus(String storageStatus) {
    this.storageStatus = storageStatus;
    return this;
  }

  public BigDecimal height() {
    return height;
  }

  public StorageUnitEntity setHeight(BigDecimal height) {
    this.height = height;
    return this;
  }

  public BigDecimal width() {
    return width;
  }

  public StorageUnitEntity setWidth(BigDecimal width) {
    this.width = width;
    return this;
  }

  public BigDecimal length() {
    return length;
  }

  public StorageUnitEntity setLength(BigDecimal length) {
    this.length = length;
    return this;
  }

  public BigDecimal weightCapacity() {
    return weightCapacity;
  }

  public StorageUnitEntity setWeightCapacity(BigDecimal weightCapacity) {
    this.weightCapacity = weightCapacity;
    return this;
  }

  public BigDecimal volumeOccupied() {
    return volumeOccupied;
  }

  public StorageUnitEntity setVolumeOccupied(BigDecimal volumeOccupied) {
    this.volumeOccupied = volumeOccupied;
    return this;
  }

  public BigDecimal weightOccupied() {
    return weightOccupied;
  }

  public StorageUnitEntity setWeightOccupied(BigDecimal weightOccupied) {
    this.weightOccupied = weightOccupied;
    return this;
  }

  public Integer maxUnits() {
    return maxUnits;
  }

  public StorageUnitEntity setMaxUnits(Integer maxUnits) {
    this.maxUnits = maxUnits;
    return this;
  }

  public Map<ProductEntity, Integer> productsWithQty() {
    return productsWithQty;
  }

  public StorageUnitEntity setProductsWithQty(Map<ProductEntity, Integer> productsWithQty) {
    this.productsWithQty = productsWithQty;
    return this;
  }

  public StorageUnitEntity addProductWithQty(ProductEntity product, int quantity) {
    this.productsWithQty.put(product, quantity);
    return this;
  }

  public StorageUnitEntity getAttachedEntity() {
    if (this.id == null) {
      return this;
    }
    try {
      return getEntityManager().merge(this);
    } catch (Exception e) {
      return this;
    }
  }
}
