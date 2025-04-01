package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;

@Entity(name = "stocks")
@Table(name = "stocks")
public class StockEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String barcode;
  private LocalDate expirationDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @Cascade(CascadeType.ALL)
  @JoinColumn(name = "product_id")
  private ProductEntity product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "storage_unit_id", nullable = true)
  private StorageUnitEntity storageUnit;

  public Long id() {
    return id;
  }

  public StockEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String barcode() {
    return barcode;
  }

  public StockEntity setBarcode(String barcode) {
    this.barcode = barcode;
    return this;
  }

  public LocalDate expirationDate() {
    return expirationDate;
  }

  public StockEntity setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
    return this;
  }

  public ProductEntity product() {
    return product;
  }

  public StorageUnitEntity storageUnit() {
    return storageUnit;
  }

  public StockEntity setProduct(ProductEntity product) {
    this.product = product;
    return this;
  }

  public StockEntity setStorageUnit(StorageUnitEntity storageUnit) {
    this.storageUnit = storageUnit;
    return this;
  }

  public StockEntity getAttachedEntity() {
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
