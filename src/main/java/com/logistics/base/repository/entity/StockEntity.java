package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "stocks")
public class StockEntity extends PanacheEntity {
    @Column(unique = true)
    public String barcode;
    public LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public ProductEntity product;
}
