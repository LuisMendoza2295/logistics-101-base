package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "storage")
public class StorageUnit extends PanacheEntity {
    @Column(unique = true)
    private String uuid;
    private String storageType;
    private String storageStatus;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal length;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
    private Integer maxUnits;
    @ManyToMany
    @JoinTable(
            name = "storage_product",
            joinColumns = { @JoinColumn(name = "storage_id")},
            inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private Set<ProductEntity> products;
}
