package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity extends PanacheEntity {
    @Column(unique=true)
    private String uuid;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal length;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
}
