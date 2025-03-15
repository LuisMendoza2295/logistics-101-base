package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;

@Entity(name = "stocks")
@Table(name = "stocks")
public class StockEntity extends PanacheEntity {
    @Column(unique = true)
    public String barcode;
    public LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "product_id")
    public ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "storage_unit_id")
    public StorageUnitEntity storageUnit;
}
