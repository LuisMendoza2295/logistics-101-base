package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity extends PanacheEntity {
  @Column(unique = true)
  public String uuid;
  public String name;
  public BigDecimal width;
  public BigDecimal height;
  public BigDecimal length;
  public BigDecimal netWeight;
  public BigDecimal grossWeight;
}
