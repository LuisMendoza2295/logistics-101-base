package com.logistics.base.repository.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "transfers")
public class TransferEntity extends PanacheEntity {
    @Column(unique = true)
    public String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    public StorageUnitEntity sourceStorage;

    @ManyToOne(fetch = FetchType.LAZY)
    public StorageUnitEntity targetStorage;

//    @ManyToMany
//    @JoinTable(
//            name = "transfer_products",
//            joinColumns = { @JoinColumn(name = "transfer_id")},
//            inverseJoinColumns = { @JoinColumn(name = "product_id") }
//    )
//    public Set<ProductEntity> productsWithQty;

    @ElementCollection
    @JoinTable(
        name = "transfer_products",
        joinColumns = {@JoinColumn(name = "transfer_id")}
    )
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    public Map<ProductEntity, Integer> productsWithQty;
}
