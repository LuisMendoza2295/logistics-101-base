package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "storage_units")
public class StorageUnitEntity extends PanacheEntity {
    @Column(unique = true)
    public String uuid;
    public String storageType;
    public String storageStatus;
    public BigDecimal height;
    public BigDecimal width;
    public BigDecimal length;
    public BigDecimal weightCapacity;
    public BigDecimal volumeOccupied;
    public BigDecimal weightOccupied;
    public Integer maxUnits;
//    @ManyToMany
//    @JoinTable(
//        name = "product_storage_unit",
//        joinColumns = {@JoinColumn(name = "storage_id")},
//        inverseJoinColumns = {@JoinColumn(name = "product_id")}
//    )
    @ElementCollection
    @JoinTable(
        name = "storage_products",
        joinColumns = {@JoinColumn(name = "storage_id")}
    )
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    public Map<ProductEntity, Integer> productsWithQty;
}
