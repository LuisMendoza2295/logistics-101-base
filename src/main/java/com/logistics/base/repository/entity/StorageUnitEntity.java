package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(
    name = "storage_units",
    indexes = {
        @Index(name = "idx_storage_type", columnList = "storageType"),
        @Index(name = "idx_storage_status", columnList = "storageStatus")
    })
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
//        name = "stock_storage_unit",
//        joinColumns = {@JoinColumn(name = "storage_id")},
//        inverseJoinColumns = {@JoinColumn(name = "stock_id")}
//    )
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "storage_unit_id")
    public Set<StockEntity> stocks;

//    @ElementCollection
//    @JoinTable(
//        name = "storage_products",
//        joinColumns = {@JoinColumn(name = "storage_id")}
//    )
//    @MapKeyJoinColumn(name = "product_id")
//    @Column(name = "quantity")
//    public Map<ProductEntity, Integer> productsWithQty;
}
