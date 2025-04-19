package com.logistics.base.infrastructure.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "products")
@Table(name = "products")
public class ProductEntity extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String uuid;
  @Column(unique = true)
  private String name;
  private BigDecimal width;
  private BigDecimal height;
  private BigDecimal length;
  private BigDecimal netWeight;
  private BigDecimal grossWeight;

  public Long id() {
    return id;
  }

  public ProductEntity setId(Long id) {
    this.id = id;
    return this;
  }

  public String uuid() {
    return uuid;
  }

  public ProductEntity setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public String name() {
    return name;
  }

  public ProductEntity setName(String name) {
    this.name = name;
    return this;
  }

  public BigDecimal width() {
    return width;
  }

  public ProductEntity setWidth(BigDecimal width) {
    this.width = width;
    return this;
  }

  public BigDecimal height() {
    return height;
  }

  public ProductEntity setHeight(BigDecimal height) {
    this.height = height;
    return this;
  }

  public BigDecimal length() {
    return length;
  }

  public ProductEntity setLength(BigDecimal length) {
    this.length = length;
    return this;
  }

  public BigDecimal netWeight() {
    return netWeight;
  }

  public ProductEntity setNetWeight(BigDecimal netWeight) {
    this.netWeight = netWeight;
    return this;
  }

  public BigDecimal grossWeight() {
    return grossWeight;
  }

  public ProductEntity setGrossWeight(BigDecimal grossWeight) {
    this.grossWeight = grossWeight;
    return this;
  }

  public ProductEntity getAttachedEntity() {
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
