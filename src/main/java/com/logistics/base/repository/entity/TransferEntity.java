package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "transfers")
@Table(name = "transfers")
public class TransferEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String uuid;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_storage_unit_id", nullable = false)
  private StorageUnitEntity sourceStorage;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_storage_unit_id", nullable = false)
  private StorageUnitEntity targetStorage;

  @ManyToMany
  @JoinTable(
    name = "transfer_stocks",
    joinColumns = {@JoinColumn(name = "transfer_id")},
    inverseJoinColumns = {@JoinColumn(name = "stock_id")}
  )
  private Set<StockEntity> stocks = new HashSet<>();

  public Long id() {
    return id;
  }

  public TransferEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String uuid() {
    return uuid;
  }

  public TransferEntity setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public StorageUnitEntity sourceStorage() {
    return sourceStorage;
  }

  public TransferEntity setSourceStorage(StorageUnitEntity sourceStorage) {
    this.sourceStorage = sourceStorage;
    return this;
  }

  public StorageUnitEntity targetStorage() {
    return targetStorage;
  }

  public TransferEntity setTargetStorage(StorageUnitEntity targetStorage) {
    this.targetStorage = targetStorage;
    return this;
  }

  public Set<StockEntity> stocks() {
    return stocks;
  }

  public TransferEntity setStocks(Set<StockEntity> stocks) {
    this.stocks = stocks;
    return this;
  }

  public TransferEntity addStock(StockEntity stockEntity) {
    stocks.add(stockEntity);
    return this;
  }

  public TransferEntity getAttachedEntity() {
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
